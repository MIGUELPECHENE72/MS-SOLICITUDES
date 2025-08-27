package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.usecase.tiposolicitud.TipoSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoSolicitudUseCase tipoSolicitudUseCase;

    public Mono<Solicitud> create(Solicitud solicitud) {
        return tipoSolicitudUseCase.getById(solicitud.getTipo())
                .flatMap(tipoSolicitud -> solicitudRepository.save(solicitud))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de solicitud no existe")));
    }

    public Mono<Solicitud> update(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    public Flux<Solicitud> getAll(){
        return solicitudRepository.findAll();
    }

    public Mono<Solicitud> getById(Long id){
        return solicitudRepository.findById(id);
    }

}
