package co.com.bancolombia.model.solicitud.gateways;

import co.com.bancolombia.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface SolicitudRepository {

    Mono<Solicitud> findById(Long id);

    Flux<Solicitud> findAll();

    Mono<Solicitud> save(Solicitud solicitud);

    Flux<Solicitud> findByEstadoIn(List<Integer> idEstados, Integer page, Integer size);

    Mono<BigDecimal> sumCuotaMensualByIdentificacionAndEstado(String identificacion, Integer estado);
}
