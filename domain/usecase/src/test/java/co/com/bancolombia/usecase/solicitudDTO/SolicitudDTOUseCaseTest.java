package co.com.bancolombia.usecase.solicitudDTO;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.persona.Persona;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.usecase.estadosolicitud.EstadoSolicitudUseCase;
import co.com.bancolombia.usecase.persona.PersonaUseCase;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import co.com.bancolombia.usecase.solicitudDTO.dto.SolicitudDTO;
import co.com.bancolombia.usecase.tiposolicitud.TipoSolicitudUseCase;
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
public class SolicitudDTOUseCaseTest {

    @InjectMocks
    SolicitudDTOUseCase solicitudDTOUseCase;

    @Mock
    SolicitudUseCase solicitudUseCase;

    @Mock
    EstadoSolicitudUseCase estadoSolicitudUseCase;

    @Mock
    TipoSolicitudUseCase tipoSolicitudUseCase;

    @Mock
    PersonaUseCase personaUseCase;

    private static final Long TEST_ID = 1L;
    private static final TipoSolicitud tipo = new TipoSolicitud(1,"Credito Libre Consumo","S");
    private static final EstadoSolicitud estado = new EstadoSolicitud(1,"Pendiente de revisión","S");
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
                    "1007779304",
                    3);
    private static final Solicitud solitud = new Solicitud(TEST_ID,
            "1007779304",
            new BigDecimal(1500000),
            12,
            1,
            1);

    @Test
    void mustGetById(){

        when(solicitudUseCase.getById(TEST_ID)).thenReturn(Mono.just(solitud));
        when(estadoSolicitudUseCase.getById(1)).thenReturn(Mono.just(estado));
        when(tipoSolicitudUseCase.getById(1)).thenReturn(Mono.just(tipo));

        Mono<SolicitudDTO> result = solicitudDTOUseCase.getById(TEST_ID);

        StepVerifier.create(result)
                .assertNext(solicitudDTO -> {
                    assertNotNull(solicitudDTO);
                    assertEquals(TEST_ID, solicitudDTO.getSolicitud().getId());
                })
                .verifyComplete();
    }

    @Test
    void mustGetBySolicitud(){

        when(estadoSolicitudUseCase.getById(1)).thenReturn(Mono.just(estado));
        when(tipoSolicitudUseCase.getById(1)).thenReturn(Mono.just(tipo));
        when(personaUseCase.obtenerUsuarioPorIdentificacion("1007779304","adjhsbaknoAjak"))
                .thenReturn(Mono.just(persona));

        Mono<SolicitudDTO> result = solicitudDTOUseCase.getBySolicitud(Mono.just(solitud),"adjhsbaknoAjak");

        StepVerifier.create(result)
                .assertNext(solicitudDTO -> {
                    assertNotNull(solicitudDTO);
                    assertEquals(TEST_ID, solicitudDTO.getSolicitud().getId());
                })
                .verifyComplete();
    }

}
