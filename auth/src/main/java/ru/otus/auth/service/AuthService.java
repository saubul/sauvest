package ru.otus.auth.service;

import ru.otus.auth.dto.TokenResponseDto;
import ru.otus.auth.dto.UserDto;

public interface AuthService {

    void registrate(UserDto userDto);

    TokenResponseDto authenticate(UserDto userDto);

    TokenResponseDto refreshToken(String authorizationHeader);

}
