package co.com.bancolombia.model.tiposolicitud.gateways;

import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoSolicitudRepository {

    Mono<TipoSolicitud> findById(Integer id);

    Flux<TipoSolicitud> findAll();

    //Mono<TipoSolicitud> save(TipoSolicitud tipoSolicitud);

}
