package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.model.notificacion.Notificacion;
import co.com.bancolombia.model.notificacion.gateways.NotificacionPublisher;
import co.com.bancolombia.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements NotificacionPublisher {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequestEmail(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequestEmail(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrlEmail())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<String> send(Notificacion notificacion) {
        log.info("Se obtiene el mensaje: {}",notificacion.getMensaje());
        return Mono.fromCallable(() -> buildRequestEmail(notificacion.getMensaje()))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequestCalcularCapacidad(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrlCalcularCapacidad())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<String> sendCalcularCapacidad(Notificacion notificacion) {
        log.info("Se obtiene el mensaje: {}",notificacion.getMensaje());
        return Mono.fromCallable(() -> buildRequestCalcularCapacidad(notificacion.getMensaje()))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequestNotifyAprobado(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrlNotifyAprobado())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<String> sendNotifyAprobado(Notificacion notificacion) {
        log.info("Se obtiene el mensaje: {}",notificacion.getMensaje());
        return Mono.fromCallable(() -> buildRequestNotifyAprobado(notificacion.getMensaje()))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }
}
