package ru.sauvest.auth.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.sauvest.auth.dto.TokenResponseDto;
import ru.sauvest.auth.dto.UserDto;
import ru.sauvest.auth.dto.UsernamePasswordDto;
import ru.sauvest.auth.service.impl.AuthServiceImpl;
import ru.sauvest.auth.service.impl.UserService;

@SpringBootTest
@Transactional
class AuthServiceImplTest {

    @Autowired
    AuthServiceImpl authServiceImpl;

    @Autowired
    UserService userService;

    @MockitoBean
    JavaMailSender javaMailSender;

    UserDto userDto;

    @BeforeEach
    void setup() {
        userDto = UserDto.builder()
                .username("admin")
                .password("admin")
                .email("admin@yandex.ru")
                .build();
    }

    @Test
    void signUp() {
        Assertions.assertDoesNotThrow(() -> authServiceImpl.signUp(userDto));
    }

    @Test
    void createToken() {
        UsernamePasswordDto usernamePasswordDto = UsernamePasswordDto.builder().username("admin").password("admin").build();
        userService.save(userDto);
        TokenResponseDto tokenResponseDto = authServiceImpl.createToken(usernamePasswordDto);

        Assertions.assertNotNull(tokenResponseDto.getAccessToken());
        Assertions.assertNotNull(tokenResponseDto.getRefreshToken());
        Assertions.assertEquals("admin", tokenResponseDto.getUsername());
    }

    @Test
    void refreshToken() {
        UsernamePasswordDto usernamePasswordDto = UsernamePasswordDto.builder().username("admin").password("admin").build();
        userService.save(userDto);
        TokenResponseDto tokenResponseDto = authServiceImpl.createToken(usernamePasswordDto);

        String refreshToken = tokenResponseDto.getRefreshToken();
        TokenResponseDto refreshedToken = authServiceImpl.refreshToken("Bearer " + refreshToken);

        Assertions.assertNotNull(refreshedToken.getAccessToken());
    }
}