package ru.otus.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.auth.config.ApplicationTestConfig;
import ru.otus.auth.config.properties.SauvestProperties;
import ru.otus.auth.dto.TokenResponseDto;
import ru.otus.auth.dto.UserDto;
import ru.otus.auth.service.AuthService;

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
    void registrate() throws Exception {
        UserDto userDto = UserDto.builder().username("admin").password("admin").build();
        mockMvc.perform(
                post("/api/v1/" + sauvestProperties.getApplicationName() + "/registrate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
        ).andExpect(status().isOk());
    }

    @Test
    void authenticate() throws Exception {
        UserDto userDto = UserDto.builder().username("admin").password("admin").build();
        Mockito.when(authService.authenticate(userDto)).thenReturn(TokenResponseDto.builder().build());
        mockMvc.perform(
                post("/api/v1/" + sauvestProperties.getApplicationName() + "/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
        ).andExpect(status().isOk());
    }

    @Test
    void refreshToken() throws Exception {
        Mockito.when(authService.refreshToken("Bearer ")).thenReturn(TokenResponseDto.builder().build());
        mockMvc.perform(
                post("/api/v1/" + sauvestProperties.getApplicationName() + "/token/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
        ).andExpect(status().isOk());
    }
}