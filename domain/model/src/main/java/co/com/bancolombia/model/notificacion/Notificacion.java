package co.com.bancolombia.model.notificacion;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Notificacion {

    private String mensaje;

    private String status;

}
