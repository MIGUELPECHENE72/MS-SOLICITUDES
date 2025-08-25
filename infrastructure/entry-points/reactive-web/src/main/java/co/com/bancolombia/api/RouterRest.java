package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("api/v1/solicitud/{id}"), handler::listenGetSolicitudById)
                .andRoute(POST("api/v1/solicitud"), handler::listenSaveSolicitud)
                .andRoute(PUT("api/v1/solicitud"), handler::listenUpdateSolicitud)
                .and(route(GET("api/v1/solicitud"), handler::listenGetAllSolicitudes));
    }
}
