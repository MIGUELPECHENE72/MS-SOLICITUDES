package co.com.bancolombia.usecase.solicitudDTO.dto;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {

    private Solicitud solicitud;

    private TipoSolicitud tipo;

    private EstadoSolicitud estado;

    private Persona persona;

    public SolicitudDTO(Solicitud solicitud, TipoSolicitud tipo, EstadoSolicitud estado) {
        this.solicitud = solicitud;
        this.tipo = tipo;
        this.estado = estado;
    }
}
