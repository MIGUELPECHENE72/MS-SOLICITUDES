package co.com.bancolombia.usecase.tiposolicitud;

import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.model.tiposolicitud.gateways.TipoSolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TipoSolicitudUseCase {

    private final TipoSolicitudRepository tipoSolicitudRepository;

    public Flux<TipoSolicitud> getAll(){
        return tipoSolicitudRepository.findAll();
    }

    public Mono<TipoSolicitud> getById(Integer id){
        return tipoSolicitudRepository.findById(id);
    }
}
