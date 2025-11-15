package com.prueba.cuentas.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CuentasExceptionHandler {
    /**
     * Handles MangasException when a requested resource is not found.
     *
     * @param ex      the MangasException thrown when the resource is not found
     * @param request the WebRequest in which the exception occurred
     * @return a ResponseEntity containing a map with error details, including the HTTP status as NOT_FOUND
     */
    @ExceptionHandler(CuentasException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(final Exception ex, final WebRequest request) {
        return handleException(ex, request, determineHttpStatus(ex));
    }

    /**
     * Handles any unhandled exception that occurs in the application.
     *
     * @param ex      the exception that was thrown
     * @param request the web request in which the exception occurred
     * @return a ResponseEntity containing a map with error details and the appropriate HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(final Exception ex, final WebRequest request) {
        HttpStatus status = determineHttpStatus(ex);
        return handleException(ex, request, status);
    }

    private ResponseEntity<Map<String, Object>> handleException(final Exception ex, final WebRequest request, final HttpStatus status) {
        logRequestAndException(request, ex);
        return new ResponseEntity<>(buildResponseBody(ex, status), status);
    }

    private void logRequestAndException(final WebRequest request, final Exception ex) {
        log.warn(request.getDescription(Boolean.TRUE));
        log.error(ex.getMessage());
    }

    private Map<String, Object> buildResponseBody(final Exception ex, final HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("message", determineMessage(ex));
        responseBody.put("status", status.value());
        return responseBody;
    }

    private HttpStatus determineHttpStatus(final Exception ex) {
        if (ex instanceof HttpRequestMethodNotSupportedException || ex instanceof MethodArgumentNotValidException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof CuentasException){
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String determineMessage(final Exception ex) {
        if (ex instanceof CuentasException || ex instanceof HttpRequestMethodNotSupportedException) {
            return ex.getMessage();
        } else if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            List<String> errors = new ArrayList<>();
            methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
                String errorMessage = error.getDefaultMessage();
                errors.add(errorMessage);
            });
            return errors.toString();
        }
        else {
            return "Error inesperado. Contacte con el soporte si el problema persiste.";
        }
    }
}
