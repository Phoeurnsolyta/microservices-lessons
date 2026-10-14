package kh.edu.istad.platform.business.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Map;

import static reactor.netty.http.HttpConnectionLiveness.log;

@RestControllerAdvice
public class BusinessExceptionHandler extends RuntimeException {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleValidation(
            WebExchangeBindException exception
    ) {
        String message = exception.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Request validation failed");

        return Mono.just(ResponseEntity.badRequest()
                .body(Map.of("message", message)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleInvalidArgument(
            IllegalArgumentException exception
    ) {
        return Mono.just(ResponseEntity.badRequest()
                .body(Map.of("message", exception.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleUnexpectedError(
            Exception exception
    ) {
        log.error("Unexpected error while processing business request", exception);

        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "An unexpected error occurred"))
        );
    }
}
