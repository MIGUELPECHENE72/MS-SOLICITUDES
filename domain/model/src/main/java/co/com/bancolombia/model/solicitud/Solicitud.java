package co.com.bancolombia.model.solicitud;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {

    private Long id;

    private String identificacion;

    private BigDecimal monto;

    private Integer plazo;

    private Integer tipo;

    private Integer estado;

    private BigDecimal tasaInteresMensual;

    private BigDecimal cuotaMensual;

}
