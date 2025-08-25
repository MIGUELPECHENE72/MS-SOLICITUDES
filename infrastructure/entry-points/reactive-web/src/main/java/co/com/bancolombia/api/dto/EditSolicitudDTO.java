package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSolicitudDTO {

    @NotNull(message = "El id es obligatorio")
    private Long id;

    @NotBlank(message = "La identificacion es obligatoria")
    private String identificacion;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    @NotNull(message = "El plazo es obligatorio")
    private Integer plazo;

    @NotNull(message = "El tipo es obligatorio")
    private Integer tipo;

    @NotNull(message = "El estado es obligatorio")
    private Integer estado;

}
