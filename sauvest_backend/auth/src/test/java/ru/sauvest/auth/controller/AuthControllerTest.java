package ru.sauvest.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.sauvest.auth.config.ApplicationTestConfig;
import ru.sauvest.auth.dto.TokenResponseDto;
import ru.sauvest.auth.dto.UserDto;
import ru.sauvest.auth.dto.UsernamePasswordDto;
import ru.sauvest.auth.service.AuthService;
import ru.sauvest.baseservices.properties.SauvestProperties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApplicationTestConfig.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthService authService;

    @Autowired
    SauvestProperties sauvestProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticate() throws Exception {
        UsernamePasswordDto usernamePasswordDto = UsernamePasswordDto.builder().username("admin").password("admin").build();
        Mockito.when(authService.createToken(usernamePasswordDto)).thenReturn(TokenResponseDto.builder().build());
        mockMvc.perform(
                post("/" + sauvestProperties.getApplicationName() + "/auth/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usernamePasswordDto))
        ).andExpect(status().isOk());
    }

    @Test
    void refreshToken() throws Exception {
        Mockito.when(authService.refreshToken("Bearer ")).thenReturn(TokenResponseDto.builder().build());
        mockMvc.perform(
                post("/" + sauvestProperties.getApplicationName() + "/auth/api/v1/token/update")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
        ).andExpect(status().isOk());
    }

    @Test
    void signUp() throws Exception {
        UserDto userDto = UserDto.builder().username("admin")
                .password("admin")
                .email("example@yandex.ru")
                .build();
        mockMvc.perform(
                post("/" + sauvestProperties.getApplicationName() + "/auth/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
        ).andExpect(status().isOk());
    }
}