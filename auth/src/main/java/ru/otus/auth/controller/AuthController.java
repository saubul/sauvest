package ru.otus.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.auth.dto.TokenResponseDto;
import ru.otus.auth.dto.UserDto;
import ru.otus.auth.service.AuthService;

@RestController
@RequestMapping("/api/v1/${sauvest.applicationName}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrate")
    public ResponseEntity<Void> registrate(@RequestBody UserDto userDto) {
        authService.registrate(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> authenticate(@RequestBody UserDto userDto) {
        TokenResponseDto tokenResponseDTO = authService.authenticate(userDto);
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        TokenResponseDto tokenResponseDTO = authService.refreshToken(authorizationHeader);
        return ResponseEntity.ok(tokenResponseDTO);
    }

}
