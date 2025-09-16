package co.com.bancolombia.usecase.notificacion;

import co.com.bancolombia.model.notificacion.Notificacion;
import co.com.bancolombia.model.notificacion.gateways.NotificacionPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionUseCaseTest {

    @InjectMocks
    NotificacionUseCase notificacionUseCase;

    @Mock
    NotificacionPublisher notificacionPublisher;

    Notificacion createNotificacion(){
        return new Notificacion("Hola mundo!","SEND");
    }

    @Test
    void mustSend(){

        Notificacion notificacion = createNotificacion();

        when(notificacionPublisher.send(notificacion)).thenReturn(Mono.just("a2ba2f5b-3bd8-4d8f-b99f-4f8b3e19eb85"));

        Mono<String> result = notificacionUseCase.send(notificacion);

        StepVerifier.create(result)
                .assertNext(id -> {
                    assertNotNull(id);
                    assertEquals("a2ba2f5b-3bd8-4d8f-b99f-4f8b3e19eb85", id);
                })
                .verifyComplete();
    }

    @Test
    void mustSendCalcularCapacidad(){

        Notificacion notificacion = createNotificacion();

        when(notificacionPublisher.sendCalcularCapacidad(notificacion))
                .thenReturn(Mono.just("a2ba2f5b-3bd8-4d8f-b99f-4f8b3e19eb85"));

        Mono<String> result = notificacionUseCase.sendCalcularCapacidad(notificacion);

        StepVerifier.create(result)
                .assertNext(id -> {
                    assertNotNull(id);
                    assertEquals("a2ba2f5b-3bd8-4d8f-b99f-4f8b3e19eb85", id);
                })
                .verifyComplete();
    }
}
