package co.com.bancolombia.model.solicitud.gateways;

import co.com.bancolombia.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {

    Mono<Solicitud> findById(Long id);

    Flux<Solicitud> findAll();

    Mono<Solicitud> save(Solicitud solicitud);

}
