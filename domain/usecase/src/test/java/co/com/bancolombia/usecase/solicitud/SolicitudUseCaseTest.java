package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.usecase.persona.PersonaUseCase;
import co.com.bancolombia.usecase.tiposolicitud.TipoSolicitudUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {

    @InjectMocks
    SolicitudUseCase solicitudUseCase;

    @Mock
    SolicitudRepository solicitudRepository;

    @Mock
    TipoSolicitudUseCase tipoSolicitudUseCase;

    @Mock
    PersonaUseCase personaUseCase;

    private static final Long TEST_ID = 1L;
    private static final String IDENTIFICACION = "1007779304";
    private static final BigDecimal VALOR = BigDecimal.valueOf(1000000);

    private Solicitud createTestSolicitud(Long id) {
        return new Solicitud(id, IDENTIFICACION, VALOR, 12, 1, 1,
                new BigDecimal("0.0214"), new BigDecimal("100000"));
    }

    private TipoSolicitud createTipoSolicitud(Integer id, String automatico){
        return new TipoSolicitud(1,"Credito Libre Consumo","S",automatico);
    }

    @Test
    void mustGetById(){

        Solicitud solicitud = createTestSolicitud(TEST_ID);

        when(solicitudRepository.findById(TEST_ID)).thenReturn(Mono.just(solicitud));

        Mono<Solicitud> result = solicitudUseCase.getById(TEST_ID);

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(IDENTIFICACION, solicitudConsulta.getIdentificacion());
                })
                .verifyComplete();
    }

    @Test
    void mustCreate(){

        Solicitud solicitud = createTestSolicitud(TEST_ID);
        TipoSolicitud tipoSolicitud = createTipoSolicitud(1,"N");

        when(solicitudRepository.save(solicitud)).thenReturn(Mono.just(solicitud));
        when(tipoSolicitudUseCase.getById(1)).thenReturn(Mono.just(tipoSolicitud));

        Mono<Solicitud> result = solicitudUseCase.create(solicitud,
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDA3Nzc5MzA1Iiwicm9sZXMiOlsiUk9MRV9BU0VTT1IiXSwia" +
                        "WF0IjoxNzU3NzA3NzY4LCJleHAiOjE3NTc3MTEzNjh9.h8jV5Tbtzw1si2cRDElTj1v9HAiUk6HnOW_CyygDT5Y");

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    //assertNotNull(solicitudConsulta);
                    assertEquals(IDENTIFICACION, solicitudConsulta.getIdentificacion());
                })
                .verifyComplete();
    }

    @Test
    void mustGetByEstadoIn(){

        List<Integer> estados = List.of(1,2,3);
        Solicitud solicitud1 = createTestSolicitud(TEST_ID);
        Solicitud solicitud2 = createTestSolicitud(TEST_ID + 1);

        when(solicitudRepository.findByEstadoIn(estados,0,2)).thenReturn(Flux.just(solicitud1, solicitud2));

        Flux<Solicitud> result = solicitudUseCase.getByEstadoIn(estados,0,2);

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(1L, solicitudConsulta.getId());
                })
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(2L, solicitudConsulta.getId());
                })
                .verifyComplete();
    }

    @Test
    void mustUpdate(){

        Solicitud solicitud = createTestSolicitud(TEST_ID);

        when(solicitudRepository.save(solicitud)).thenReturn(Mono.just(solicitud));

        Mono<Solicitud> result = solicitudUseCase.update(solicitud);

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    //assertNotNull(solicitudConsulta);
                    assertEquals(IDENTIFICACION, solicitudConsulta.getIdentificacion());
                })
                .verifyComplete();
    }

    @Test
    void mustGetAll(){

        Solicitud solicitud1 = createTestSolicitud(TEST_ID);
        Solicitud solicitud2 = createTestSolicitud(TEST_ID + 1);

        when(solicitudRepository.findAll()).thenReturn(Flux.just(solicitud1, solicitud2));

        Flux<Solicitud> result = solicitudUseCase.getAll();

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(1L, solicitudConsulta.getId());
                })
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(2L, solicitudConsulta.getId());
                })
                .verifyComplete();
    }

    @Test
    void mustAprobarManual(){

        Solicitud solicitud = createTestSolicitud(TEST_ID);
        solicitud.setEstado(2);

        when(solicitudUseCase.update(solicitud)).thenReturn(Mono.just(solicitud));

        Mono<Solicitud> result = solicitudUseCase.aprobarManual(solicitud);

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    //assertNotNull(solicitudConsulta);
                    assertEquals(IDENTIFICACION, solicitudConsulta.getIdentificacion());
                })
                .verifyComplete();
    }

    @Test
    void mustAprobarManualError(){

        Solicitud solicitud = createTestSolicitud(TEST_ID);

        Mono<Solicitud> result = solicitudUseCase.aprobarManual(solicitud);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void mustCalcularCuotaMensual(){

        Solicitud solicitud = createTestSolicitud(TEST_ID);

        solicitud.setPlazo(12);
        solicitud.setMonto(new BigDecimal("5000000"));
        solicitud.setTasaInteresMensual(new BigDecimal("0.0214"));

        Mono<Solicitud> result = solicitudUseCase.calcularCuotaMensual(solicitud);

        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(new BigDecimal("476872.48"), solicitudConsulta.getCuotaMensual());
                })
                .verifyComplete();
    }
    @Test
    void mustAprobarAutomaticoErrorNoValido() {

        Long id = 1L;
        Map<String, Integer> updates = new HashMap<>();
        updates.put("estado", 1);

        Mono<Solicitud> result = solicitudUseCase.aprobarAutomatico(id, updates);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();

    }

    @Test
    void mustAprobarAutomaticoErrorNoExiste() {

        Long id = 1L;
        Map<String, Integer> updates = new HashMap<>();
        updates.put("name", 1);

        Mono<Solicitud> result = solicitudUseCase.aprobarAutomatico(id, updates);

        StepVerifier.create(result)
                .expectError(NoSuchElementException.class)
                .verify();

    }

    @Test
    void mustAprobarAutomaticoTres() {

        Map<String, Integer> updates = new HashMap<>();
        updates.put("estado", 3);
        Solicitud solicitud = createTestSolicitud(TEST_ID);
        Solicitud solicitudModificado = createTestSolicitud(TEST_ID);
        solicitudModificado.setEstado(3);

        when(solicitudRepository.findById(TEST_ID)).thenReturn(Mono.just(solicitud));
        when(solicitudRepository.save(solicitud)).thenReturn(Mono.just(solicitudModificado));

        Mono<Solicitud> result = solicitudUseCase.aprobarAutomatico(TEST_ID, updates);

        StepVerifier.create(result)
                .assertNext(saved -> {
                    assertNotNull(saved);
                    assertEquals(3, saved.getEstado());
                })
                .verifyComplete();

    }

    @Test
    void mustAprobarAutomaticoDos() {

        Map<String, Integer> updates = new HashMap<>();
        updates.put("estado", 2);
        Solicitud solicitud = createTestSolicitud(TEST_ID);
        Solicitud solicitudModificado = createTestSolicitud(TEST_ID);
        solicitudModificado.setEstado(2);

        when(solicitudRepository.findById(TEST_ID)).thenReturn(Mono.just(solicitud));
        when(solicitudRepository.save(solicitud)).thenReturn(Mono.just(solicitudModificado));

        Mono<Solicitud> result = solicitudUseCase.aprobarAutomatico(TEST_ID, updates);

        StepVerifier.create(result)
                .assertNext(saved -> {
                    assertNotNull(saved);
                    assertEquals(2, saved.getEstado());
                })
                .verifyComplete();

    }

    @Test
    void mustAprobarAutomaticoCuatro() {

        Map<String, Integer> updates = new HashMap<>();
        updates.put("estado", 4);
        Solicitud solicitud = createTestSolicitud(TEST_ID);
        Solicitud solicitudModificado = createTestSolicitud(TEST_ID);
        solicitudModificado.setEstado(4);

        when(solicitudRepository.findById(TEST_ID)).thenReturn(Mono.just(solicitud));
        when(solicitudRepository.save(solicitud)).thenReturn(Mono.just(solicitudModificado));

        Mono<Solicitud> result = solicitudUseCase.aprobarAutomatico(TEST_ID, updates);

        StepVerifier.create(result)
                .assertNext(saved -> {
                    assertNotNull(saved);
                    assertEquals(4, saved.getEstado());
                })
                .verifyComplete();

    }
}
