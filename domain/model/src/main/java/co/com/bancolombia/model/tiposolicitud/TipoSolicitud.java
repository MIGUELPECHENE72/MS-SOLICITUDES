package co.com.bancolombia.model.tiposolicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoSolicitud {

    private Integer id;

    private String nombre;

    private String estado;

    private String validacionAutomatica;

}
