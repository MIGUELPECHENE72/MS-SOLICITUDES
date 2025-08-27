package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {
    @Bean
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
