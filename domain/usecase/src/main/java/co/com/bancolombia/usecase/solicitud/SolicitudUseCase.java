package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.notificacion.Notificacion;
import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.usecase.notificacion.NotificacionUseCase;
import co.com.bancolombia.usecase.persona.PersonaUseCase;
import co.com.bancolombia.usecase.solicitud.enums.Estado;
import co.com.bancolombia.usecase.tiposolicitud.TipoSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoSolicitudUseCase tipoSolicitudUseCase;
    private final PersonaUseCase personaUseCase;
    private final NotificacionUseCase notificacionUseCase;

    public Mono<Solicitud> create(Solicitud solicitud, String token) {
        return tipoSolicitudUseCase.getById(solicitud.getTipo())
                .flatMap(tipoSolicitud -> calcularCuotaMensual(solicitud)
                            .flatMap(solicitudCuota -> solicitudRepository.save(solicitudCuota)
                                    .flatMap(saved -> envioAprobarAutomatico(saved,tipoSolicitud, token)))
                )
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de solicitud no existe")));
    }

    public Mono<Solicitud> update(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    public Mono<Solicitud> aprobarManual(Solicitud solicitud){
        return (solicitud.getEstado().equals(4) || solicitud.getEstado().equals(2))? update(solicitud) :
                Mono.error(new IllegalArgumentException("Solo se puede aprobar o rechazar solicitudes"));
    }

    public Mono<Solicitud> aprobarAutomatico(Long id, Map<String, Integer> updates){
        if(updates.containsKey("estado")){
            Integer estado = updates.get("estado");
            return (estado.equals(Estado.REVISION_MANUAL.valor) ||
                    estado.equals(Estado.APROVADA.valor) ||
                    estado.equals(Estado.RECHAZADA.valor))?
                    getById(id).flatMap(solicitud -> {
                        solicitud.setEstado(estado);
                        return update(solicitud);
                    }) :
                    Mono.error(new IllegalArgumentException("Solo son validos los estados: " +
                            "Revision manual, Aprobada y Rechazada"));
        }return Mono.error(new NoSuchElementException("La solicitud no contiene el estado"));
    }

    public Flux<Solicitud> getAll(){
        return solicitudRepository.findAll();
    }

    public Mono<Solicitud> getById(Long id){
        return solicitudRepository.findById(id);
    }

    public Flux<Solicitud> getByEstadoIn(List<Integer> idEstados, Integer page, Integer size){
        return solicitudRepository.findByEstadoIn(idEstados, page, size);
    }

    public Mono<Solicitud> calcularCuotaMensual(Solicitud solicitud) {

        BigDecimal pasoUno = BigDecimal.ONE.add(solicitud.getTasaInteresMensual()); // 1 + i
        BigDecimal pasoDos = pasoUno.pow(solicitud.getPlazo(), MathContext.DECIMAL128);  // (1 + i)^n
        BigDecimal pasoTres = solicitud.getTasaInteresMensual().multiply(pasoDos, MathContext.DECIMAL128); // i * (1 + i)^n
        BigDecimal pasoCuatro = solicitud.getMonto().multiply(pasoTres, MathContext.DECIMAL128); //P * (i * (1 + i)^n)
        BigDecimal pasoCinco = pasoDos.subtract(BigDecimal.ONE); //(1 + i)^n - 1

        // [P * (i * (1 + i)^n)] / [(1 + i)^n - 1]
        BigDecimal cuotaMensual = pasoCuatro.divide(pasoCinco, MathContext.DECIMAL128);
        solicitud.setCuotaMensual(cuotaMensual.setScale(2, RoundingMode.HALF_UP));

        return Mono.just(solicitud);
    }

    public Mono<Solicitud> envioAprobarAutomatico(Solicitud solicitud, TipoSolicitud tipoSolicitud, String token) {
        if (tipoSolicitud.getValidacionAutomatica().equals("S")) {

            Mono<BigDecimal> deudaMensualActualMono =
                    solicitudRepository.sumCuotaMensualByIdentificacionAndEstado(solicitud.getIdentificacion(), 4);

            Mono<Persona> personaMono =
                    personaUseCase.obtenerUsuarioPorIdentificacion(solicitud.getIdentificacion(), token);

            return deudaMensualActualMono.zipWhen(deudaMensualActual -> personaMono)
                    .flatMap(tuple -> {
                        BigDecimal deudaMensualActual = tuple.getT1();
                        Persona persona = tuple.getT2();

                        String json = aprobarAutomaticoSESJSON(deudaMensualActual,
                                persona.getNombres(),
                                persona.getCorreoElectronico(),
                                persona.getSalarioBase(),
                                solicitud.getCuotaMensual(),
                                solicitud.getId(),
                                solicitud.getPlazo(),
                                solicitud.getMonto(),
                                solicitud.getTasaInteresMensual());

                        // Aquí puedes hacer lo que necesites con el JSON (como enviarlo a un servicio)
                        notificacionUseCase.sendCalcularCapacidad(new Notificacion(json,"SENDING"))
                                .doOnSuccess(messageId ->
                                        System.out.println("Respuesta sqs: " + messageId))
                                .doOnError(error ->
                                        System.out.print("Error al enviar mensaje a SQS: " + error.getMessage()))
                                .subscribe();

                        return Mono.just(solicitud);
                    });
        }
        return Mono.just(solicitud);
    }


    public String aprobarAutomaticoSESJSON(BigDecimal deudaMensualActual,
                                           String nombre,
                                           String email,
                                           BigDecimal ingresosTotales,
                                           BigDecimal cuotaSolicitud,
                                           Long idSolicitud,
                                           Integer plazoSolicitud,
                                           BigDecimal montoSolicitud,
                                           BigDecimal tasaInteresMensualSolicitud){
        return String.format("""
                {
                    "deudaMensualActual": "%s",
                    "nombre": "%s",
                    "email": "%s",
                    "ingresosTotales": "%s",
                    "cuotaSolicitud": "%s",
                    "idSolicitud": "%s",
                    "plazoSolicitud": "%s",
                    "montoSolicitud": "%s",
                    "tasaInteresMensualSolicitud": "%s",
                }
                """,deudaMensualActual,nombre,email,ingresosTotales,cuotaSolicitud,idSolicitud,
                plazoSolicitud,montoSolicitud,tasaInteresMensualSolicitud);
    }
}
