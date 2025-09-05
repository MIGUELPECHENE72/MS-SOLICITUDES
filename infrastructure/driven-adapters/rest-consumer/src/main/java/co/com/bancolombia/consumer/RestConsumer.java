package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.mapper.PersonaMapper;
import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaServicePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RestConsumer implements PersonaServicePort {
    private final WebClient client;

    private final PersonaMapper personaMapper;


    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
    @CircuitBreaker(name = "testGet" /*, fallbackMethod = "testGetOk"*/)
    public Mono<Persona> testGet(String identificacion) {
        return client
                .get()
                .uri("/api/v1/usuarios/identificacion/{identificacion}", identificacion)
                .retrieve()
                .bodyToMono(ObjectResponse.class)
                .map(personaMapper::toModel)
                .onErrorResume(e -> Mono.error(new NoSuchElementException("Error consultando persona.")));
    }

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get() // TODO: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    @CircuitBreaker(name = "testPost")
    public Mono<ObjectResponse> testPost() {
        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), ObjectRequest.class)
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

    @Override
    public Mono<Persona> obtenerUsuarioPorId(String identificacion, String token) {
        return client
                .get()
                .uri("/api/v1/usuarios/identificacion/{identificacion}", identificacion)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(ObjectResponse.class)
                .map(personaMapper::toModel)
                .onErrorResume(e -> Mono.error(new NoSuchElementException("Error consultando persona.")));
    }
}
