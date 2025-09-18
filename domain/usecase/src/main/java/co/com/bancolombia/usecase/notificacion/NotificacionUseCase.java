package co.com.bancolombia.usecase.notificacion;

import co.com.bancolombia.model.notificacion.Notificacion;
import co.com.bancolombia.model.notificacion.gateways.NotificacionPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NotificacionUseCase {

    private final NotificacionPublisher notificacionPublisher;

    public Mono<String> send(Notificacion notificacion){
        return notificacionPublisher.send(notificacion);
    }

    public Mono<String> sendCalcularCapacidad(Notificacion notificacion){
        return notificacionPublisher.sendCalcularCapacidad(notificacion);
    }

    public Mono<String> sendNotifyAprobado(Notificacion notificacion){
        return notificacionPublisher.sendNotifyAprobado(notificacion);
    }

}
