package co.com.bancolombia.usecase.estadosolicitud;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoSolicitudUseCaseTest {

    @InjectMocks
    EstadoSolicitudUseCase estadoSolicitudUseCase;

    @Mock
    EstadoSolicitudRepository estadoSolicitudRepository;

    private static final Integer TEST_ID = 1;
    private static final EstadoSolicitud estado1 = new EstadoSolicitud(1,"Pendiente de revisión","S");
    private static final EstadoSolicitud estado2 = new EstadoSolicitud(2,"Rechazada","S");

    @Test
    void mustGetAll(){

        when(estadoSolicitudRepository.findAll()).thenReturn(Flux.just(estado1, estado2));

        Flux<EstadoSolicitud> result = estadoSolicitudUseCase.getAll();

        StepVerifier.create(result)
                .assertNext(estadoConsulta -> {
                    assertNotNull(estadoConsulta);
                    assertEquals(1, estadoConsulta.getId());
                })
                .assertNext(estadoConsulta -> {
                    assertNotNull(estadoConsulta);
                    assertEquals(2, estadoConsulta.getId());
                })
                .verifyComplete();
    }

    @Test
    void mustGetById(){

        when(estadoSolicitudRepository.findById(TEST_ID)).thenReturn(Mono.just(estado1));

        Mono<EstadoSolicitud> result = estadoSolicitudUseCase.getById(TEST_ID);

        StepVerifier.create(result)
                .assertNext(estadoConsulta -> {
                    assertNotNull(estadoConsulta);
                    assertEquals("Pendiente de revisión", estadoConsulta.getNombre());
                })
                .verifyComplete();
    }

}
