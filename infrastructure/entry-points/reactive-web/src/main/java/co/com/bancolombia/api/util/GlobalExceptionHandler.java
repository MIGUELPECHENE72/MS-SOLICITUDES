package co.com.bancolombia.api.util;

import co.com.bancolombia.api.util.exception.DuplicateRecordException;
import co.com.bancolombia.api.util.exception.ErrorResponse;
import co.com.bancolombia.api.util.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Log4j2
@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        log.error("Excepción capturada: {}", ex.getMessage(),ex);
        HttpStatus status;

        if(ex instanceof ResourceNotFoundException || ex instanceof NoSuchElementException){
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof DuplicateRecordException || ex instanceof IllegalArgumentException) {
            status = HttpStatus.CONFLICT;
        } else if (ex  instanceof SecurityException){
            status = HttpStatus.UNAUTHORIZED;
        }else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),status.getReasonPhrase());

        try{
            String body = mapper.writeValueAsString(errorResponse);
            exchange.getResponse().setStatusCode(status);

            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        }catch (Exception e) {
            return Mono.error(e);
        }
    }

}
