package co.com.bancolombia.model.estadosolicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EstadoSolicitud {

    private Integer id;

    private String nombre;

    private String estado;

}
