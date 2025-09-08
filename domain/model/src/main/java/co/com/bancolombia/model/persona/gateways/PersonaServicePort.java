package co.com.bancolombia.model.persona.gateways;

import co.com.bancolombia.model.persona.Persona;
import reactor.core.publisher.Mono;

public interface PersonaServicePort {
    Mono<Persona> obtenerUsuarioPorId(String identificacion, String token);
}
