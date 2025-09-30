package com.webcrafters.helpify.excepciones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponde> handleResponseStatusException(ResponseStatusException ex) {
        log.error("Error occurred", ex);
        ErrorResponde error = new ErrorResponde(ex.getStatusCode().value(), ex.getReason());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponde> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Error occurred", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.NOT_FOUND.value(), "Recurso no encontrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponde> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("Error occurred", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.NOT_FOUND.value(), "Recurso no encontrado");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponde> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error occurred", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponde> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String campo = error.getField();
            String mensaje = error.getDefaultMessage();
            errores.computeIfAbsent(campo, key -> new ArrayList<>()).add(mensaje);
        });

        ErrorResponde error = new ErrorResponde(HttpStatus.BAD_REQUEST.value(), errores.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponde> handleGeneralExceptionRuntime(RuntimeException ex) {
        log.error("Error occurred", ex);// para el programador
        ErrorResponde error = new ErrorResponde(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Intentelo de nuevo");//para el cliente
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponde> handleGeneralException(Exception ex) {
        log.error("Error occurred", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Exception Ocurrió un error inesperado");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
