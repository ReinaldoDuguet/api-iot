package com.grupouno.iot.minero.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiKeyAlreadyExistsException.class)
    public ResponseEntity<Object> handleApiKeyAlreadyExists(ApiKeyAlreadyExistsException ex) {
        Map<String, Object> response = Map.of(
                "message", ex.getMessage(),
                "details", "La api_key que intentas registrar ya está asociada a otra entidad.",
                "status", HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		Map<String, Object> response = Map.of(
				"message", ex.getMessage(),
				"status", HttpStatus.NOT_FOUND.value()
		);

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
    
    @ExceptionHandler(EntityAlreadyExistsException.class)
	public ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
		Map<String, Object> response = Map.of(
				"message", ex.getMessage(),
				"status", HttpStatus.UNAUTHORIZED.value()
		);

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
    
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
		Map<String, Object> response = Map.of(
				"message", ex.getMessage(),
				"status", HttpStatus.BAD_REQUEST.value()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
	public ResponseEntity<Object> handleJpaEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {
		return new ResponseEntity<>(Map.of(
				"message", ex.getMessage(),
				"status", HttpStatus.NOT_FOUND.value()
		), HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequest(InvalidRequestException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
