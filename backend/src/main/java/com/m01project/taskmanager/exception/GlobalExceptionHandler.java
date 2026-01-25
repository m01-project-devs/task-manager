package com.m01project.taskmanager.exception;

import com.m01project.taskmanager.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class,
            BoardNotFoundException.class})
    public ResponseEntity<?> handleNotFound(RuntimeException ex) {
        ErrorResponseDto error = createResponse(ex, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRoleAssignmentException.class)
    public ResponseEntity<?> handleInvalidRoleAssignment(InvalidRoleAssignmentException ex) {
        ErrorResponseDto error = createResponse(ex, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({UserAlreadyExistsException.class,
                      EmailAlreadyUsedException.class,
                      DuplicateBoardTitleException.class})
    public ResponseEntity<?> handleConflict(RuntimeException ex) {
        ErrorResponseDto error = createResponse(ex, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(Exception ex) {
        ErrorResponseDto error = createResponse(ex, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                "Access denied - insufficient permissions",
                HttpStatus.FORBIDDEN.value()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDto error = createResponse(ex, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDto error = createResponse(ex, HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                "Required request body is missing or malformed",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        ErrorResponseDto error = createResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidTokenException.class,
                       TokenAlreadyUsedException.class,
                       TokenExpiredException.class})
    public ResponseEntity<ErrorResponseDto> handleInvalidToken(RuntimeException ex){
        ErrorResponseDto error = createResponse(ex, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    public ErrorResponseDto createResponse(Exception ex, int status) {
        return new ErrorResponseDto(
                LocalDateTime.now(),
                ex.getMessage(),
                status
        );
    }
}
