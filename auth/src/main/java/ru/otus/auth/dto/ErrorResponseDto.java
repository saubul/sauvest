package ru.otus.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {

    private String code;

    private String message;

}
