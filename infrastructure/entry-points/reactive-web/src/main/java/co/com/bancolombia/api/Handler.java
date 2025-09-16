package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateSolicitudDTO;
import co.com.bancolombia.api.dto.EditSolicitudDTO;
import co.com.bancolombia.api.dto.SolicitudDTO;
import co.com.bancolombia.api.mapper.SolicitudDTOMapper;
import co.com.bancolombia.api.security.JwtTokenProvider;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.api.util.exception.ResourceNotFoundException;
import co.com.bancolombia.model.notificacion.Notificacion;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.usecase.notificacion.NotificacionUseCase;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import co.com.bancolombia.usecase.solicitudDTO.SolicitudDTOUseCase;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class Handler {

    private final SolicitudUseCase solicitudUseCase;

    private final SolicitudDTOUseCase solicitudDTOUseCase;

    private final NotificacionUseCase notificacionUseCase;

    private final SolicitudDTOMapper solicitudDTOMapper;

    private final RequestValidator requestValidator;

    private final TransactionalOperator transactionalOperator;

    private final JwtTokenProvider jwtTokenProvider;

    public Mono<ServerResponse> listenGetSolicitudById(ServerRequest serverRequest) {

        Long id = Long.parseLong(serverRequest.pathVariable("id"));

        return solicitudUseCase.getById(id)
                .flatMap(solicitud -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(solicitudDTOMapper.toResponse(solicitud))
                )
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("No se ha encontrado una solicitud con el id: " + id)));

    }

    public Mono<ServerResponse> listenGetSolicitudDTOById(ServerRequest serverRequest) {

        Long id = Long.parseLong(serverRequest.pathVariable("id"));

        return solicitudDTOUseCase.getById(id)
                .flatMap(solicitudDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(
                                //solicitudDTOMapper.toResponse(solicitud)
                                solicitudDTO
                        )
                )
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("No se ha encontrado una solicitud con el id: " + id)));

    }

    public Mono<ServerResponse> listenGetSolicitudesByEstados(ServerRequest serverRequest) {

        List<Integer> idEstados = Arrays.stream(serverRequest.pathVariable("idEstados").split(","))
                .map(Integer::parseInt)
                .toList();

        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));

        log.info("****Consulta por los estados: {}, página: {}, tamaño: {}", idEstados.toString(), page, size);

        return solicitudUseCase.getByEstadoIn(idEstados, page, size)
                .flatMap(solicitud ->
                        solicitudDTOUseCase.getBySolicitud(Mono.just(solicitud),getToken(serverRequest)))
                .collectList()
                .flatMap(solicitudesDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(solicitudesDTO));
    }

    public Mono<ServerResponse> listenGetAllSolicitudes(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(solicitudUseCase.getAll(), SolicitudDTO.class);
    }

    public Mono<ServerResponse> listenSaveSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateSolicitudDTO.class)
                .doOnSubscribe(subscription -> log.info("******Inicia llamado a crear solicitud"))
                .flatMap(createSolicitudDTO ->
                        requestValidator.validadorSolicitud(createSolicitudDTO)
                                .flatMap(validatedDTO -> {
                                    if (validatedDTO.getIdentificacion()
                                            .equals(jwtTokenProvider.getUsernameFromToken(getToken(serverRequest)))) {
                                        return solicitudUseCase.create(
                                                solicitudDTOMapper.toModel(validatedDTO),getToken(serverRequest))
                                                .transform(transactionalOperator::transactional);
                                    } else {
                                        return Mono.error(
                                                new ValidationException("La solicitud debe ser para usted mismo"));
                                    }
                                })
                                .flatMap(saved -> ServerResponse.ok()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(solicitudDTOMapper.toResponse(saved))
                                )
                )
                .doOnTerminate(() -> log.info("*****Finalizó el proceso de creación de la solicitud."));
    }

    public Mono<ServerResponse> listenUpdateSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EditSolicitudDTO.class)
                .doOnSubscribe(subscription -> log.info("******Inicia llamado a actualizar solicitud"))
                .flatMap(editSolicitudDTO ->
                        requestValidator.validadorSolicitud(editSolicitudDTO)
                                .flatMap(validatedDTO -> solicitudUseCase.update(
                                        solicitudDTOMapper.toModel(validatedDTO))
                                        .transform(transactionalOperator::transactional)
                                )
                                .flatMap(saved -> ServerResponse.ok()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(solicitudDTOMapper.toResponse(saved))
                                )
                )
                .doOnTerminate(() -> log.info("*****Finalizó el proceso de actualización de la solicitud."));
    }

    public Mono<ServerResponse> listenAprobarSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EditSolicitudDTO.class)
                .doOnSubscribe(subscription -> log.info("******Inicia llamado a aprobar solicitud"))
                .flatMap(editSolicitudDTO ->
                        requestValidator.validadorSolicitud(editSolicitudDTO)
                                .flatMap(validatedDTO -> {

                                    Mono<Solicitud> saved = solicitudUseCase.aprobarManual(
                                                            solicitudDTOMapper.toModel(validatedDTO))
                                            .transform(transactionalOperator::transactional);

                                    return solicitudDTOUseCase.getBySolicitud(saved,getToken(serverRequest))
                                            .flatMap(completo ->
                                                sendNotification(
                                                        "Novedad en el estado de su solicitud de credito",
                                                        completo.getPersona().getCorreoElectronico(),
                                                        "<p>El estado de su solicitud de credito ahora es: " +
                                                                completo.getEstado().getNombre() + "</p>"
                                                        ).then(saved)
                                            );
                                    }
                                )
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(solicitudDTOMapper.toResponse(result))
                                )
                )
                .doOnTerminate(() -> log.info("*****Finalizó el proceso de aprobar la solicitud."));
    }

    private Mono<String> sendNotification(String subject, String to, String body) {
        String mensaje = String.format("""
            {
                "subject": "%s",
                "to": "%s",
                "bodyEmail": "%s"
            }
            """, subject, to, body);
        return notificacionUseCase.send(new Notificacion(mensaje, "SENDING"))
                .doOnSuccess(messageId ->
                        log.info("Respuesta sqs: {}", messageId))
                .doOnError(error ->
                        log.error("Error al enviar mensaje a SQS: {}", error.getMessage(), error));
    }

    private String getToken(ServerRequest serverRequest){
        return serverRequest.headers().header("Authorization").stream()
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .findFirst()
                .map(authHeader -> authHeader.substring(7))
                .orElseThrow(() -> new IllegalArgumentException("Token JWT no encontrado"));
    }

}
