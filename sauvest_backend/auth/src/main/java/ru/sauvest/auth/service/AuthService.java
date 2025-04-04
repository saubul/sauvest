package ru.sauvest.auth.service;

import ru.sauvest.auth.dto.TokenResponseDto;
import ru.sauvest.auth.dto.UserDto;
import ru.sauvest.auth.dto.UsernamePasswordDto;
import ru.sauvest.auth.dto.VerificationTokenDto;

public interface AuthService {

    TokenResponseDto createToken(UsernamePasswordDto usernamePasswordDto);

    TokenResponseDto refreshToken(String authorizationHeader);

    UserDto signUp(UserDto userDto);

    Boolean activateAccount(VerificationTokenDto verificationTokenDto);

    Boolean isAccountEnabled(String username);

    Boolean isTokenValid(String token);

    Boolean checkAccountHasRole(String username, String roleName);

}
