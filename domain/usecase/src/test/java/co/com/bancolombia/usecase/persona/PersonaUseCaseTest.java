package co.com.bancolombia.usecase.persona;

import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.persona.gateways.PersonaServicePort;
import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonaUseCaseTest {

    @InjectMocks
    PersonaUseCase personaUseCase;

    @Mock
    PersonaServicePort personaServicePort;

    private static final String TEST_ID = "1007779304";
    private static final String TEST_TOKEN = "bjhsbkNKSAJ";
    private static final Persona persona =
            new Persona(1,
                    "MIGUEL ANGEL",
                    "PECHENE PECHENE",
                    LocalDate.now(),
                    "PT MADERO",
                    "3233820787",
                    "miguelpechene72@gmail.com",
                    new BigDecimal("3000000"),
                    1,
                    TEST_ID,
                    3);

    @Test
    void mustObtenerUsuarioPorIdentificacion(){

        when(personaServicePort.obtenerUsuarioPorId(TEST_ID,TEST_TOKEN)).thenReturn(Mono.just(persona));

        Mono<Persona> result = personaUseCase.obtenerUsuarioPorIdentificacion(TEST_ID,TEST_TOKEN);

        StepVerifier.create(result)
                .assertNext(personaConsulta -> {
                    assertNotNull(personaConsulta);
                    assertEquals(TEST_ID, personaConsulta.getIdentificacion());
                })
                .verifyComplete();
    }
}
