package ru.otus.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.otus.auth.config.properties.JWTProperties;
import ru.otus.auth.dto.TokenResponseDto;
import ru.otus.auth.dto.UserDto;
import ru.otus.auth.exception.SauvestAuthException;
import ru.otus.auth.util.JwtUtilService;

import java.util.Date;

import static ru.otus.auth.exception.ExceptionCodeConstants.*;

@Service
@RequiredArgsConstructor
public class JWTAuthService implements AuthService {

    private final JWTProperties jwtProperties;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Override
    public void registrate(UserDto userDto) {
        validateRegistrationDto(userDto);
        userService.save(userDto);
    }

    @Override
    public TokenResponseDto authenticate(UserDto userDto) {
        validateAuthenticationRequest(userDto);
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())).getPrincipal();
        } catch (AuthenticationException authenticationException) {
            throw new SauvestAuthException(AUTHENTICATION_ERROR, "При попытке аутентифицировать пользователя произошла ошибка: " +
                    "пользователь не аутентифицирован");
        }

        long jwtExpirationTime = jwtProperties.getExpirationTime().getSeconds() * 1000;

        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());

        Date accessTokenExpiresAtDate = new Date(System.currentTimeMillis() + jwtExpirationTime);
        String accessToken = JwtUtilService.getJwtToken(userDetails, accessTokenExpiresAtDate, algorithm);

        Date refreshTokenExpiresAtDate = new Date(System.currentTimeMillis() + jwtExpirationTime * 10);
        String refreshToken = JwtUtilService.getJwtToken(userDetails, refreshTokenExpiresAtDate, algorithm);

        return TokenResponseDto.builder()
                .username(userDetails.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponseDto refreshToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret().getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();

                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getClaim("username").asString();
                UserDetails user = userService.loadUserByUsername(username);

                long jwtExpirtationTime = jwtProperties.getExpirationTime().getSeconds() * 1000 * 10;
                Date accessTokenExpiresAtDate = new Date(System.currentTimeMillis() + jwtExpirtationTime);
                String accessToken = JwtUtilService.getJwtToken(user, accessTokenExpiresAtDate, algorithm);

                return TokenResponseDto.builder().accessToken(accessToken).build();
            } catch (TokenExpiredException tokenExpiredException) {
                throw new SauvestAuthException(INVALID_TOKEN, "При попытке обновить токен произошла ошибка проверки refresh-токена: срок годности истёк");
            } catch (JWTVerificationException jwtVerificationException) {
                throw new SauvestAuthException(INVALID_TOKEN, "При попытке обновить токен произошла ошибка проверки refresh-токена: " + jwtVerificationException.getMessage());
            }
        }
        throw new SauvestAuthException(WRONG_AUTHORIZATION_HEADER,
                String.format("При попытке обновить токен произошла ошибка: в заголовок \"%s\" запроса передано некорректное значение \"%s\"",
                        HttpHeaders.AUTHORIZATION, authorizationHeader));
    }

    private void validateAuthenticationRequest(UserDto userDto) {
        if (StringUtils.isEmpty(userDto.getUsername())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке аутентифицировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передано пустое имя пользователя");
        }
        if (StringUtils.isEmpty(userDto.getPassword())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке аутентифицировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передан пустой пароль пользователя");
        }
    }

    private void validateRegistrationDto(UserDto userDto) {
        if (StringUtils.isEmpty(userDto.getUsername())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке зарегистрировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передано пустое имя пользователя");
        }
        if (StringUtils.isEmpty(userDto.getPassword())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке зарегистрировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передан пустой пароль пользователя");
        }
    }
}
