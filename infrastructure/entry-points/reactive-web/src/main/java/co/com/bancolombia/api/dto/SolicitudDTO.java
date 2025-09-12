package co.com.bancolombia.api.dto;

import java.math.BigDecimal;

public record SolicitudDTO(Long id,
                            String identificacion,
                            BigDecimal monto,
                            Integer plazo,
                            Integer tipo,
                            Integer estado,
                            BigDecimal tasaInteresMensual,
                            BigDecimal cuotaMensual) {
}
