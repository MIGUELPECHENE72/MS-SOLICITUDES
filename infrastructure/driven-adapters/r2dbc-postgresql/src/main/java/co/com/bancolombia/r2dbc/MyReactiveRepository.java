package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.r2dbc.entity.solicitud.SolicitudEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    Flux<Solicitud> findByEstadoIn(List<Integer> idEstados, Pageable pageable);

    @Query("""
            SELECT SUM(cuota_mensual) FROM crediya.solicitud 
            WHERE identificacion = :identificacion AND id_estado_solicitud = :estado
            """)
    Mono<BigDecimal> sumCuotaMensualByIdentificacionAndEstado(String identificacion, Integer estado);
}
