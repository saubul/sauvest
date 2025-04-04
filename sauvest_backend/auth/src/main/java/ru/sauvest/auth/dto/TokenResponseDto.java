package ru.sauvest.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDto {

    private final String accessToken;

    private final String refreshToken;

    private final String username;

}
