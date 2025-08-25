package co.com.bancolombia.api.util;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validadorSolicitud(T dto) {
        return Mono.fromCallable(() -> {

            var errors = new BeanPropertyBindingResult(dto, dto.getClass().getName());
            validator.validate(dto, errors);

            // Si hay errores de validación, extraemos los mensajes de error y lanzamos una excepción personalizada
            if (errors.hasErrors()) {
                List<String> errorMessages = errors.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage()) // Extraemos solo el mensaje de error
                        .collect(Collectors.toList());

                // Lanzamos una excepción personalizada con los mensajes de error
                throw new ValidationException(String.join(", ", errorMessages)); // Mensajes concatenados
            }

            return dto;
        });
    }

}
