package ru.isu.observer.responses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.isu.observer.security.JwtAuthenticationException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<String> handleConflict(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
