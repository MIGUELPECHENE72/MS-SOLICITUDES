package co.com.bancolombia.sqs.listener;

import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final SolicitudUseCase solicitudUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        System.out.println(message.body());

        //solicitudUseCase.aprobarAutomatico();
        return Mono.empty();
        // return myUseCase.doAny(message.body());
    }
}
