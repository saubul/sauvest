package ru.otus.auth.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.auth.dto.TokenResponseDto;
import ru.otus.auth.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JWTAuthServiceTest {

    @Autowired
    JWTAuthService jwtAuthService;

    @Autowired
    UserService userService;

    @Test
    void registrate() {
        UserDto userDto = UserDto.builder().username("admin").password("admin").build();
        Assertions.assertDoesNotThrow(() -> jwtAuthService.registrate(userDto));
    }

    @Test
    void authenticate() {
        UserDto userDto = UserDto.builder().username("admin").password("admin").build();
        userService.save(userDto);
        TokenResponseDto tokenResponseDto = jwtAuthService.authenticate(userDto);

        Assertions.assertNotNull(tokenResponseDto.getAccessToken());
        Assertions.assertNotNull(tokenResponseDto.getRefreshToken());
        Assertions.assertEquals("admin", tokenResponseDto.getUsername());
    }

    @Test
    void refreshToken() {
        UserDto userDto = UserDto.builder().username("admin").password("admin").build();
        userService.save(userDto);
        TokenResponseDto tokenResponseDto = jwtAuthService.authenticate(userDto);

        String refreshToken = tokenResponseDto.getRefreshToken();
        TokenResponseDto refreshedToken = jwtAuthService.refreshToken("Bearer " + refreshToken);

        Assertions.assertNotNull(refreshedToken.getAccessToken());
    }
}