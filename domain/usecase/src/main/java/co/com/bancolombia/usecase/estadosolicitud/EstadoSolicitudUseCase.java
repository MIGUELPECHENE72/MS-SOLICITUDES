package co.com.bancolombia.usecase.estadosolicitud;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EstadoSolicitudUseCase {

    private final EstadoSolicitudRepository estadoSolicitudRepository;

    public Flux<EstadoSolicitud> getAll(){
        return estadoSolicitudRepository.findAll();
    }

    public Mono<EstadoSolicitud> getById(Integer id){
        return estadoSolicitudRepository.findById(id);
    }

}
