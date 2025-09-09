package co.com.bancolombia.usecase.tiposolicitud;

import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.model.tiposolicitud.gateways.TipoSolicitudRepository;
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
public class TipoSolicitudUseCaseTest {

    @InjectMocks
    TipoSolicitudUseCase tipoSolicitudUseCase;

    @Mock
    TipoSolicitudRepository tipoSolicitudRepository;

    private static final Integer TEST_ID = 1;
    private static final TipoSolicitud tipo1 = new TipoSolicitud(1,"Credito Libre Consumo","S");
    private static final TipoSolicitud tipo2 = new TipoSolicitud(2,"Credito Vivienda","S");

    @Test
    void mustGetAll(){

        when(tipoSolicitudRepository.findAll()).thenReturn(Flux.just(tipo1, tipo2));

        Flux<TipoSolicitud> result = tipoSolicitudUseCase.getAll();

        StepVerifier.create(result)
                .assertNext(tipoConsulta -> {
                    assertNotNull(tipoConsulta);
                    assertEquals(1, tipoConsulta.getId());
                })
                .assertNext(tipoConsulta -> {
                    assertNotNull(tipoConsulta);
                    assertEquals(2, tipoConsulta.getId());
                })
                .verifyComplete();
    }

    @Test
    void mustGetById(){

        when(tipoSolicitudRepository.findById(TEST_ID)).thenReturn(Mono.just(tipo1));

        Mono<TipoSolicitud> result = tipoSolicitudUseCase.getById(TEST_ID);

        StepVerifier.create(result)
                .assertNext(tipoConsulta -> {
                    assertNotNull(tipoConsulta);
                    assertEquals("Credito Libre Consumo", tipoConsulta.getNombre());
                })
                .verifyComplete();
    }
}
