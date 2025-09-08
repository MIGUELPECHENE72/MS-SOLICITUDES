package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.CreateSolicitudDTO;
import co.com.bancolombia.api.dto.EditSolicitudDTO;
import co.com.bancolombia.api.dto.SolicitudDTO;
import co.com.bancolombia.model.solicitud.Solicitud;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitudDTOMapper {

    SolicitudDTO toResponse(Solicitud solicitud);

    List<SolicitudDTO> toResponseList(List<Solicitud> solicitudes);

    Solicitud toModel(CreateSolicitudDTO createSolicitudDTO);

    Solicitud toModel(EditSolicitudDTO editSolicitudDTO);

}
