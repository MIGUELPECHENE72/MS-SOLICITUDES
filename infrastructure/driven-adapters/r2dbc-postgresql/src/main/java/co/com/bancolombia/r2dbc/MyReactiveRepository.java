package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.r2dbc.entity.solicitud.SolicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import org.springframework.data.domain.Pageable;
import java.util.List;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    Flux<Solicitud> findByEstadoIn(List<Integer> idEstados, Pageable pageable);

}
