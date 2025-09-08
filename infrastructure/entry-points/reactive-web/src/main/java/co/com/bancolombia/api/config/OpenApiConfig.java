package co.com.bancolombia.api.config;

import co.com.bancolombia.api.Handler;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    static {
        SpringDocUtils.getConfig().addRestControllers(Handler.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Solicitudes")
                        .description("API para gestionar solicitudes en el sistema")
                        .version("1.0")
                );
    }

}
