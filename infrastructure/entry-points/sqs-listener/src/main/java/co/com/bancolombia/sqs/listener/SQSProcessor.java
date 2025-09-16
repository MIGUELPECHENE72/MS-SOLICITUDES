package co.com.bancolombia.sqs.listener;

import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Log4j2
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final SolicitudUseCase solicitudUseCase;

    @Override
    public Mono<Void> apply(Message message) {

        log.info("Se recibe: {}", message.body());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(message.body(), Map.class);

            Long idSolicitud = ((Number) jsonMap.get("idSolicitud")).longValue();
            Map<String, Integer> updates = new HashMap<>();
            updates.put("estado", (Integer) jsonMap.get("estado"));
            return solicitudUseCase.aprobarAutomatico(idSolicitud, updates).then(Mono.empty());
        } catch (Exception e) {
            log.error("Ha ocurrido un error actualizando solicitud automaticamente: {}",e.getMessage(),e);
            return Mono.error(e);
        }
    }
}
