package co.com.bancolombia.model.estadosolicitud.gateways;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadoSolicitudRepository {

    Mono<EstadoSolicitud> findById(Integer id);

    Flux<EstadoSolicitud> findAll();

    //Mono<EstadoSolicitud> save(EstadoSolicitud estadoSolicitud);

}
