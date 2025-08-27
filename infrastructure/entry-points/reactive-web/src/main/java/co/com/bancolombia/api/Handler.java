package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateSolicitudDTO;
import co.com.bancolombia.api.dto.EditSolicitudDTO;
import co.com.bancolombia.api.dto.SolicitudDTO;
import co.com.bancolombia.api.mapper.SolicitudDTOMapper;
import co.com.bancolombia.api.util.RequestValidator;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import co.com.bancolombia.usecase.solicitudDTO.SolicitudDTOUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class Handler {

    private final SolicitudUseCase solicitudUseCase;

    private final SolicitudDTOUseCase solicitudDTOUseCase;

    private final SolicitudDTOMapper solicitudDTOMapper;

    private final RequestValidator requestValidator;

    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> listenGetSolicitudById(ServerRequest serverRequest) {

        Long id = Long.parseLong(serverRequest.pathVariable("id"));

        return solicitudUseCase.getById(id)
                .flatMap(solicitud -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(solicitudDTOMapper.toResponse(solicitud))
                )
                .switchIfEmpty(ServerResponse.notFound().build()); // Si no se encuentra, devolver un 404

    }

    public Mono<ServerResponse> listenGetSolicitudDTOById(ServerRequest serverRequest) {

        Long id = Long.parseLong(serverRequest.pathVariable("id"));

        return solicitudDTOUseCase.getById(id)
                .flatMap(solicitudDTO -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(
                                //solicitudDTOMapper.toResponse(solicitud)
                                solicitudDTO
                        )
                )
                .switchIfEmpty(ServerResponse.notFound().build()); // Si no se encuentra, devolver un 404

    }

    public Mono<ServerResponse> listenGetAllSolicitudes(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(solicitudUseCase.getAll(), SolicitudDTO.class);
    }

    @Operation(summary = "Crear una nueva solicitud", description = "Este endpoint crea una nueva solicitud en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud no válida")
    })
    public Mono<ServerResponse> listenSaveSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateSolicitudDTO.class)
                .doOnSubscribe(subscription -> log.info("******Inicia llamado a crear solicitud"))
                .flatMap(createSolicitudDTO ->
                        requestValidator.validadorSolicitud(createSolicitudDTO)
                                .flatMap(validatedDTO -> solicitudUseCase.create(
                                                solicitudDTOMapper.toModel(validatedDTO))
                                        .transform(transactionalOperator::transactional)
                                )
                                .flatMap(saved -> ServerResponse.ok()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(solicitudDTOMapper.toResponse(saved))
                                )
                )
                .onErrorResume(this::handleError)
                .doOnTerminate(() -> log.info("*****Finalizó el proceso de creación de la solicitud."));
    }

    public Mono<ServerResponse> listenUpdateSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EditSolicitudDTO.class)
                .doOnSubscribe(subscription -> log.info("******Inicia llamado a actualizar solicitud"))
                .flatMap(editSolicitudDTO ->
                        requestValidator.validadorSolicitud(editSolicitudDTO)
                                .flatMap(validatedDTO -> solicitudUseCase.update(
                                        solicitudDTOMapper.toModel(validatedDTO))
                                        .transform(transactionalOperator::transactional)
                                )
                                .flatMap(saved -> ServerResponse.ok()
                                        .contentType(APPLICATION_JSON)
                                        .bodyValue(solicitudDTOMapper.toResponse(saved))
                                )
                )
                .onErrorResume(this::handleError)
                .doOnTerminate(() -> log.info("*****Finalizó el proceso de actualización de la solicitud."));
    }

    // Método centralizado para manejar errores
    private Mono<ServerResponse> handleError(Throwable e) {
        log.error("*****Ha ocurrido un error de validación: {}", e.getMessage(), e);
        return ServerResponse.badRequest()
                .contentType(APPLICATION_JSON)
                .bodyValue("Error de validación: " + e.getMessage());
    }
}
