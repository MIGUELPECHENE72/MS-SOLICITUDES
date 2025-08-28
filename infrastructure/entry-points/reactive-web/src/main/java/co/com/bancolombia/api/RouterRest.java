package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.SolicitudDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
                                            in = ParameterIn.PATH
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud encontrada",
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
                    path = "/api/v1/solicitud",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllSolicitudes",
                    operation = @Operation(
                            operationId = "crearSolicitud",
                            summary = "Crear una nueva solicitud",
                            tags = { "solicitud" },
                            //requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            //        required = true,
                            //        description = "Datos para crear solicitud"
                            //),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Solicitud creada exitosamente"),
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
