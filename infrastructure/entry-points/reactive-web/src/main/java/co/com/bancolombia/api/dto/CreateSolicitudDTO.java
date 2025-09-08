package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class CreateSolicitudDTO {

    private Long id;

    @NotBlank(message = "La identificacion es obligatoria")
    private String identificacion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 1, message = "El plazo debe ser mayor a 0")
    private Integer plazo;

    @NotNull(message = "El tipo es obligatorio")
    private Integer tipo;

    private Integer estado = 1;

}
