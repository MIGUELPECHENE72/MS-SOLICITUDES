package co.com.bancolombia.usecase.persona;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonaUseCase {

    private final PersonaServicePort personaServicePort;

    public Mono<Persona> obtenerUsuarioPorIdentificacion(String identificacion, String token){
        return personaServicePort.obtenerUsuarioPorId(identificacion, token);
    }

}
