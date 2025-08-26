package co.com.bancolombia.r2dbc.entity.solicitud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud")
public class SolicitudEntity {

    @Id
    private Long id;

    private String identificacion;

    private BigDecimal monto;

    private Integer plazo;

    @Column("id_tipo_solicitud")
    private Integer tipo;

    @Column("id_estado_solicitud")
    private Integer estado;

}
