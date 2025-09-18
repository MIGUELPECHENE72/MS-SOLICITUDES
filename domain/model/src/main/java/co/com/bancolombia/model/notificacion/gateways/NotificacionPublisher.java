package co.com.bancolombia.model.notificacion.gateways;

import co.com.bancolombia.model.notificacion.Notificacion;
import reactor.core.publisher.Mono;

public interface NotificacionPublisher {

    Mono<String> send(Notificacion notificacion);

    Mono<String> sendCalcularCapacidad(Notificacion notificacion);

    Mono<String> sendNotifyAprobado(Notificacion notificacion);

}
