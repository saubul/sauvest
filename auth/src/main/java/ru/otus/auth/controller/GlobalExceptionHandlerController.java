package ru.otus.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.auth.dto.ErrorResponseDto;
import ru.otus.auth.exception.SauvestAuthException;

import static ru.otus.auth.exception.ExceptionCodeConstants.*;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(SauvestAuthException.class)
    ResponseEntity<ErrorResponseDto> handleSauvestAuthException(SauvestAuthException sauvestAuthException) {
        String exceptionMessage = sauvestAuthException.getMessage();
        String exceptionCode = sauvestAuthException.getCode();
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(exceptionCode)
                .message(exceptionMessage)
                .build();
        HttpStatus status = resolveHttpStatus(exceptionCode);
        return new ResponseEntity<>(errorResponse, status);
    }

    private HttpStatus resolveHttpStatus(String exceptionCode) {
        return switch (exceptionCode) {
            case RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case VALIDATION_ERROR, WRONG_AUTHORIZATION_HEADER -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }


}
