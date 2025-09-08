package co.com.bancolombia.r2dbc.entity.tipoSolicitud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("tipo_solicitud")
public class TipoSolicitudEntity {

    @Id
    private Integer id;

    private String nombre;

    private String estado;

}
