package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.r2dbc.entity.solicitud.SolicitudEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private static final Long TEST_ID = 1L;
    private static final String IDENTIFICACION = "1007779304";
    private static final BigDecimal VALOR = BigDecimal.valueOf(1000000);

    private SolicitudEntity createTestSolicitudEntity(Long id) {
        return new SolicitudEntity(id, IDENTIFICACION, VALOR, 12, 1, 1);
    }

    private Solicitud createTestSolicitud(Long id) {
        return new Solicitud(id, IDENTIFICACION, VALOR, 12, 1, 1);
    }

    @Test
    void mustFindValueById() {
        // GIVEN
        SolicitudEntity solicitudEntity = createTestSolicitudEntity(TEST_ID);
        Solicitud solicitud = createTestSolicitud(TEST_ID);

        when(repository.findById(TEST_ID)).thenReturn(Mono.just(solicitudEntity));
        when(mapper.map(solicitudEntity, Solicitud.class)).thenReturn(solicitud);

        // WHEN
        Mono<Solicitud> result = repositoryAdapter.findById(TEST_ID);

        // THEN
        StepVerifier.create(result)
                .assertNext(solicitudConsulta -> {
                    assertNotNull(solicitudConsulta);
                    assertEquals(IDENTIFICACION, solicitudConsulta.getIdentificacion());
                })
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        // GIVEN
        SolicitudEntity solicitudEntity = createTestSolicitudEntity(TEST_ID);
        Solicitud solicitud = createTestSolicitud(TEST_ID);

        when(repository.save(any(SolicitudEntity.class))).thenReturn(Mono.just(solicitudEntity));
        when(mapper.map(any(Solicitud.class), any())).thenReturn(solicitudEntity);
        when(mapper.map(solicitudEntity, Solicitud.class)).thenReturn(solicitud);

        // WHEN
        Mono<Solicitud> result = repositoryAdapter.save(solicitud);

        // THEN
        StepVerifier.create(result)
                .assertNext(solicitudNueva -> {
                    assertNotNull(solicitudNueva);
                    assertEquals(IDENTIFICACION, solicitudNueva.getIdentificacion());
                })
                .verifyComplete();
    }

    @Test
    void mustFindByEstadoIn() {
        // GIVEN
        List<Integer> estados = List.of(1, 2, 3);

        SolicitudEntity solicitudEntity1 = createTestSolicitudEntity(1L);
        Solicitud solicitud1 = createTestSolicitud(1L);
        SolicitudEntity solicitudEntity2 = createTestSolicitudEntity(2L);
        Solicitud solicitud2 = createTestSolicitud(2L);

        when(repository.findByEstadoIn(anyList(), any(Pageable.class))).thenReturn(Flux.just(solicitud1, solicitud2));
        //when(mapper.map(solicitudEntity1, Solicitud.class)).thenReturn(solicitud1);
        //when(mapper.map(solicitudEntity2, Solicitud.class)).thenReturn(solicitud2);

        // WHEN
        Flux<Solicitud> result = repositoryAdapter.findByEstadoIn(estados, 1, 2);

        // THEN
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
    void mustFindByAll() {
        // GIVEN
        SolicitudEntity solicitudEntity1 = createTestSolicitudEntity(1L);
        Solicitud solicitud1 = createTestSolicitud(1L);
        SolicitudEntity solicitudEntity2 = createTestSolicitudEntity(2L);
        Solicitud solicitud2 = createTestSolicitud(2L);

        when(repository.findAll()).thenReturn(Flux.just(solicitudEntity1, solicitudEntity2));
        when(mapper.map(solicitudEntity1, Solicitud.class)).thenReturn(solicitud1);
        when(mapper.map(solicitudEntity2, Solicitud.class)).thenReturn(solicitud2);

        // WHEN
        Flux<Solicitud> result = repositoryAdapter.findAll();

        // THEN
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
}
