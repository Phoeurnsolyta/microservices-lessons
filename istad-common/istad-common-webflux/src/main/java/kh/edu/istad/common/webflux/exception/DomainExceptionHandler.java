package kh.edu.istad.common.webflux.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainExceptionHandler extends RuntimeException {

    @ExceptionHandler(DomainExceptionHandler.class)
    public ResponseEntity<>
}
