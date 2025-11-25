package com.franciscovelasco.mutantdetector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Maneja los errores más frecuentes y devuelve mensajes legibles por clientes.
 * La idea es explicar la causa raíz sin filtrar información sensible.
 *
 * @author Francisco Velasco (Legajo 51141)
 */
@ControllerAdvice
public class ApiExceptionAdvisor {

    @ExceptionHandler(GenomeFormatException.class)
    public ResponseEntity<Map<String, Object>> handleGenomeFormat(GenomeFormatException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> payload = basicPayload(HttpStatus.BAD_REQUEST);
        Map<String, String> details = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.put(error.getField(), error.getDefaultMessage());
        }
        payload.put("errors", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payload);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Se produjo un error inesperado");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> payload = basicPayload(status);
        payload.put("message", message);
        return ResponseEntity.status(status).body(payload);
    }

    private Map<String, Object> basicPayload(HttpStatus status) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", status.value());
        payload.put("timestamp", OffsetDateTime.now().toString());
        return payload;
    }
}

