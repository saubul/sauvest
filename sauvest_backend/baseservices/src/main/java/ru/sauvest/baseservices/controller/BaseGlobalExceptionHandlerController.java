package ru.sauvest.baseservices.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sauvest.baseservices.dto.ErrorResponseDto;
import ru.sauvest.baseservices.exception.SauvestException;

@RestControllerAdvice
public class BaseGlobalExceptionHandlerController {

    @ExceptionHandler(SauvestException.class)
    ResponseEntity<ErrorResponseDto> handleSauvestAuthException(SauvestException sauvestException) {
        String exceptionMessage = sauvestException.getMessage();
        String exceptionCode = sauvestException.getCode();
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(exceptionCode)
                .message(exceptionMessage)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
