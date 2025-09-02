package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateSolicitudDTO;
import co.com.bancolombia.api.dto.EditSolicitudDTO;
import co.com.bancolombia.api.dto.SolicitudDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;//1125
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud/{id}",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetSolicitudById",
                    operation = @Operation(
                            operationId = "getSolicitudById",
                            summary = "Obtener una solicitud por ID",
                            tags = { "solicitud" },
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID de la solicitud",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            example = "1"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud exitosa",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SolicitudDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Ejemplo de respuesta",
                                                            summary = "Solicitud válida",
                                                            value = """
                                                            {
                                                              "id": 1,
                                                              "identificacion": "1007779304",
                                                              "monto": 5000000,
                                                              "plazo": 12,
                                                              "tipo": 1,
                                                              "estado": 1
                                                            }
                                                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitudDTO/{id}",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetSolicitudDTOById",
                    operation = @Operation(
                            operationId = "getSolicitudDTOById",
                            summary = "Obtener una solicitudDTO por ID",
                            tags = { "solicitud" },
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            description = "ID de la solicitud",
                                            required = true,
                                            in = ParameterIn.PATH,
                                            example = "1"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud exitosa",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation =
                                                            co.com.bancolombia.usecase.solicitudDTO.dto
                                                                    .SolicitudDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Ejemplo de respuesta",
                                                            summary = "Solicitud válida",
                                                            value = """
                                                            {
                                                                "solicitud": {
                                                                    "id": 1,
                                                                    "identificacion": "1007779304",
                                                                    "monto": 5000000,
                                                                    "plazo": 12,
                                                                    "tipo": 1,
                                                                    "estado": 1
                                                                },
                                                                "tipo": {
                                                                    "id": 1,
                                                                    "nombre": "Credito Libre Consumo",
                                                                    "estado": "S"
                                                                },
                                                                "estado": {
                                                                    "id": 1,
                                                                    "nombre": "Pendiente de revisión",
                                                                    "estado": "S"
                                                                }
                                                            }
                                                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllSolicitudes",
                    operation = @Operation(
                            operationId = "getAllSolicitudes",
                            summary = "Obtener todas las solicitudes existentes en el sistema",
                            tags = { "solicitud" },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud exitosa",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SolicitudDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Ejemplo de respuesta",
                                                            summary = "Solicitud válida",
                                                            value = """
                                                            [
                                                                {
                                                                  "id": 1,
                                                                  "identificacion": "1007779304",
                                                                  "monto": 5000000,
                                                                  "plazo": 12,
                                                                  "tipo": 1,
                                                                  "estado": 1
                                                                },
                                                                {
                                                                  "id": 2,
                                                                  "identificacion": "1007779304",
                                                                  "monto": 1000000,
                                                                  "plazo": 6,
                                                                  "tipo": 2,
                                                                  "estado": 1
                                                                }
                                                            ]
                                                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveSolicitud",
                    operation = @Operation(
                            operationId = "saveSolicitud",
                            summary = "Crear una solicitud",
                            tags = { "solicitud" },
                            requestBody = @RequestBody(
                                    description = "Datos necesarios para crear una nueva solicitud",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CreateSolicitudDTO.class),
                                            examples = @ExampleObject(
                                                    name = "Ejemplo de body",
                                                    value = """
                                                    {
                                                        "identificacion": "1007779304",
                                                        "monto": 5000000,
                                                        "plazo": 12,
                                                        "tipo": 1
                                                    }
                                                    """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud exitosa",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SolicitudDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Ejemplo de respuesta",
                                                            summary = "Solicitud válida",
                                                            value = """
                                                            {
                                                              "id": 1,
                                                              "identificacion": "1007779304",
                                                              "monto": 5000000,
                                                              "plazo": 12,
                                                              "tipo": 1,
                                                              "estado": 1
                                                            }
                                                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
                                    @ApiResponse(responseCode = "400", description = "Error de validación")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = { "application/json" },
                    method = RequestMethod.PUT,
                    beanClass = Handler.class,
                    beanMethod = "listenUpdateSolicitud",
                    operation = @Operation(
                            operationId = "UpdateSolicitud",
                            summary = "Editar una solicitud",
                            tags = { "solicitud" },
                            requestBody = @RequestBody(
                                    description = "Datos necesarios para editar una solicitud",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = EditSolicitudDTO.class),
                                            examples = @ExampleObject(
                                                    name = "Ejemplo de body",
                                                    value = """
                                                    {
                                                        "id": 1,
                                                        "identificacion": "1007779304",
                                                        "monto": 5000000,
                                                        "plazo": 12,
                                                        "tipo": 1,
                                                        "estado": 1
                                                    }
                                                    """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud exitosa",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SolicitudDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Ejemplo de respuesta",
                                                            summary = "Solicitud válida",
                                                            value = """
                                                            {
                                                              "id": 1,
                                                              "identificacion": "1007779304",
                                                              "monto": 5000000,
                                                              "plazo": 12,
                                                              "tipo": 1,
                                                              "estado": 1
                                                            }
                                                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
                                    @ApiResponse(responseCode = "400", description = "Error de validación")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return RouterFunctions.route()
                .GET("api/v1/solicitud/{id}", handler::listenGetSolicitudById)
                .GET("api/v1/solicitudDTO/{id}",handler::listenGetSolicitudDTOById)
                .POST("api/v1/solicitud", handler::listenSaveSolicitud)
                .PUT("api/v1/solicitud", handler::listenUpdateSolicitud)
                .GET("api/v1/solicitud", handler::listenGetAllSolicitudes)
                .build();
    }
}
