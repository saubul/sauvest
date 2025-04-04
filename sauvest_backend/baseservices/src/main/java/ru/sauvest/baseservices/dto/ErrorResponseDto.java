package ru.sauvest.baseservices.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {

    private String code;

    private String message;

}
