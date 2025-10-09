package com.apinayami.demo.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class,
            IllegalArgumentException.class, PropertyReferenceException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            message = errors.toString();
            errorResponse.setError("Validation Failed");
        } else if (e instanceof ConstraintViolationException || e instanceof IllegalArgumentException
                || e instanceof PropertyReferenceException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorResponse.setError("PathVariable Invalid");

        }
        errorResponse.setMessage(message);

        return errorResponse;
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Invalid Parameter Type");

        String paramName = e.getName();
        String requiredType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown";
        String providedValue = e.getValue() != null ? e.getValue().toString() : "null";

        String message = String.format("Tham số '%s' không hợp lệ. Giá trị '%s' không thể chuyển đổi sang kiểu %s",
                paramName, providedValue, requiredType);

        errorResponse.setMessage(message);
        return errorResponse;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // Hoặc HttpStatus.NOT_FOUND tùy vào cách bạn xử lý
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Invalid JSON Format");

        String message = ex.getMessage();
        if (message != null && message.contains("Cannot deserialize value")) {
            // Extract field name and type mismatch info
            if (message.contains("java.lang.Long")) {
                message = "Giá trị phải là số nguyên, không được để trong dấu ngoặc kép";
            } else if (message.contains("java.lang.Double")) {
                message = "Giá trị phải là số thực, không được để trong dấu ngoặc kép";
            } else if (message.contains("java.lang.Integer")) {
                message = "Giá trị phải là số nguyên, không được để trong dấu ngoặc kép";
            } else {
                message = "Định dạng dữ liệu không hợp lệ. Vui lòng kiểm tra lại kiểu dữ liệu của các trường";
            }
        } else {
            message = "JSON không hợp lệ. Vui lòng kiểm tra lại cú pháp";
        }

        errorResponse.setMessage(message);
        return errorResponse;
    }

    // @ExceptionHandler(JwtValidationException.class)
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // public ErrorResponse handleJwtException(JwtValidationException ex) {
    // ErrorResponse errorResponse = new ErrorResponse();
    // errorResponse.setTimestamp(new Date());
    // errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    // errorResponse.setError("Bad credentials");
    // errorResponse.setMessage(ex.getMessage());
    // return errorResponse;
    // }

}
