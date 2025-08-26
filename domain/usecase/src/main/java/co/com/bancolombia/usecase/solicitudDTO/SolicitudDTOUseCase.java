package co.com.bancolombia.usecase.solicitudDTO;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.usecase.estadosolicitud.EstadoSolicitudUseCase;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import co.com.bancolombia.usecase.solicitudDTO.dto.SolicitudDTO;
import co.com.bancolombia.usecase.tiposolicitud.TipoSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudDTOUseCase {

    private final SolicitudUseCase solicitudUseCase;
    private final EstadoSolicitudUseCase estadoSolicitudUseCase;
    private final TipoSolicitudUseCase tipoSolicitudUseCase;

    public Mono<SolicitudDTO> getById(Long id) {
        return solicitudUseCase.getById(id)
                .flatMap(solicitud -> {
                    Mono<EstadoSolicitud> estado = estadoSolicitudUseCase.getById(solicitud.getEstado());
                    Mono<TipoSolicitud> tipo = tipoSolicitudUseCase.getById(solicitud.getTipo());

                    return Mono.zip(tipo, estado)
                            .map(tuple -> new SolicitudDTO(solicitud,
                                                                                            tuple.getT1(),
                                                                                            tuple.getT2()));
                });
    }

}
