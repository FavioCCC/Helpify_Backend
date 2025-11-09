package com.webcrafters.helpify.excepciones;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    private static final String MSG_400 = "La solicitud enviada al servidor contiene errores o información incompleta.";
    private static final String MSG_401 = "La autenticación es inválida o no se proporcionaron credenciales válidas.";
    private static final String MSG_401_BAD_CREDENTIALS = "Usuario o contraseña incorrectos. Intente nuevamente.";
    private static final String MSG_401_TOKEN_EXPIRED = "Token expirado. Inicie sesión nuevamente.";
    private static final String MSG_403 = "El acceso al recurso solicitado está restringido o no se tienen los permisos necesarios.";
    private static final String MSG_404 = "El recurso o elemento solicitado no se encuentra disponible en el servidor.";
    private static final String MSG_409 = "La solicitud no puede completarse debido a un conflicto con el estado actual del recurso.";
    private static final String MSG_500 = "Ocurrió un error inesperado en el servidor al procesar la solicitud.";

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponde> handleResponseStatusException(ResponseStatusException ex) {
        String mensaje = ex.getReason() != null ? ex.getReason() : ex.getMessage();
        int status = ex.getStatusCode() != null ? ex.getStatusCode().value() : 500;
        ErrorResponde body = new ErrorResponde(status, mensaje);
        return ResponseEntity.status(status).body(body);
    }


    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponde> handleBadRequestExceptions(Exception ex) {
        log.error("Bad request", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.BAD_REQUEST.value(), MSG_400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponde> handleBadCredentials(BadCredentialsException ex) {
        log.error("Bad credentials", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.UNAUTHORIZED.value(), MSG_401_BAD_CREDENTIALS);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponde> handleTokenExpired(ExpiredJwtException ex) {
        log.error("Token expired", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.UNAUTHORIZED.value(), MSG_401_TOKEN_EXPIRED);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponde> handleJwtError(JwtException ex) {
        log.error("JWT error", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.UNAUTHORIZED.value(), MSG_401);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponde> handleConflictExceptions(Exception ex) {
        log.error("Conflict or data integrity violation", ex);
        String msg = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : MSG_409;
        ErrorResponde error = new ErrorResponde(HttpStatus.CONFLICT.value(), msg);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponde> handleForbiddenExceptions(Exception ex) {
        log.error("Access denied", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.FORBIDDEN.value(), MSG_403);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponde> handleNotFoundExceptions(Exception ex) {
        log.error("Resource not found", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.NOT_FOUND.value(), MSG_404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponde> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponde> handleGeneralException(Exception ex) {
        log.error("Unhandled exception", ex);
        ErrorResponde error = new ErrorResponde(HttpStatus.INTERNAL_SERVER_ERROR.value(), MSG_500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static class ConflictException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ConflictException(String message) {
            super(message);
        }

        public ConflictException() {
            super("La solicitud no puede completarse debido a un conflicto con el estado actual del recurso.");
        }
    }
}