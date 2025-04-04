package ru.sauvest.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.sauvest.auth.dto.*;
import ru.sauvest.auth.entity.Role;
import ru.sauvest.auth.entity.User;
import ru.sauvest.auth.entity.VerificationToken;
import ru.sauvest.auth.exception.SauvestAuthException;
import ru.sauvest.auth.kafka.producer.SauvestAuthKafkaProducer;
import ru.sauvest.auth.service.AuthService;
import ru.sauvest.auth.service.MailService;
import ru.sauvest.auth.service.VerificationTokenService;
import ru.sauvest.auth.util.JwtUtilService;
import ru.sauvest.baseservices.exception.SauvestException;
import ru.sauvest.baseservices.properties.JWTProperties;
import ru.sauvest.baseservices.service.FormatUtilService;

import java.util.Date;
import java.util.List;

import static ru.sauvest.auth.exception.AuthExceptionCodeConstants.*;
import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.UNKNOWN_ERROR;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTProperties jwtProperties;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final VerificationTokenService verificationTokenService;

    private final MailService mailService;

    private final SauvestAuthKafkaProducer sauvestAuthKafkaProducer;

    @Value("${mail_server}")
    private String mail_server;

    @Override
    @Transactional
    public UserDto signUp(UserDto userDto) {
        validateRegistrationDto(userDto);
        try {
            UserDto result = userService.save(userDto);

            String verificationToken = verificationTokenService.createForUser(userDto.getUsername());
            mailService.sendMail(NotificationEmailDto.builder()
                    .recipient(userDto.getEmail())
                    .subject("SAUSOCIAL ACCOUNT ACTIVATION")
                    .body("Thank you for signing up to Sauvest, " +
                            "please click on the link below to activate your account : " +
                            "http://" + mail_server + "/successActivate/" + verificationToken)
                    .build());

            sauvestAuthKafkaProducer.sendUserRegisterEventAsync(
                    ru.sauvest.kafka.avro.model.UserRegisterEvent.UserRegisterEvent.newBuilder()
                            .setEmail(userDto.getEmail())
                            .setUsername(userDto.getUsername())
                            .setSsoToken(userDto.getSsoToken())
                            .build()
            );

            return result;
        } catch (Exception e) {
            throw new SauvestException(UNKNOWN_ERROR,
                    "При попытке регистрации пользователя произошла ошибка: " + FormatUtilService.getNonNullExceptionMessage(e),
                    e);
        }
    }

    @Override
    @Transactional
    public Boolean activateAccount(VerificationTokenDto verificationTokenDto) {
        try {
            VerificationToken verificationToken = verificationTokenService.findByToken(verificationTokenDto.getToken());
            verificationTokenService.delete(verificationToken);
            User user = userService.findByUsername(verificationToken.getUser().getUsername());
            user.setEnabled(true);
            userService.save(user);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new SauvestException(UNKNOWN_ERROR,
                    String.format("При попытке активировать аккаунт пользователя произошла ошибка: %s", FormatUtilService.getNonNullExceptionMessage(e)),
                    e
            );
        }
    }

    @Override
    public TokenResponseDto createToken(UsernamePasswordDto usernamePasswordDto) {
        validateAuthenticationRequest(usernamePasswordDto);
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernamePasswordDto.getUsername(), usernamePasswordDto.getPassword())
            ).getPrincipal();
        } catch (AuthenticationException authenticationException) {
            throw new SauvestAuthException(AUTHENTICATION_ERROR,
                    "При попытке аутентифицировать пользователя произошла ошибка: пользователь не аутентифицирован",
                    authenticationException);
        }
        checkIsUserEnabled(userDetails.isEnabled());

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
                checkIsUserEnabled(user.isEnabled());
                long jwtExpirtationTime = jwtProperties.getExpirationTime().getSeconds() * 1000 * 10;
                Date accessTokenExpiresAtDate = new Date(System.currentTimeMillis() + jwtExpirtationTime);
                String accessToken = JwtUtilService.getJwtToken(user, accessTokenExpiresAtDate, algorithm);

                return TokenResponseDto.builder().accessToken(accessToken).build();
            } catch (TokenExpiredException tokenExpiredException) {
                throw new SauvestAuthException(INVALID_TOKEN,
                        "При попытке обновить токен произошла ошибка проверки refresh-токена: срок годности истёк",
                        tokenExpiredException);
            } catch (JWTVerificationException jwtVerificationException) {
                throw new SauvestAuthException(INVALID_TOKEN,
                        "При попытке обновить токен произошла ошибка проверки refresh-токена: " + jwtVerificationException.getMessage(),
                        jwtVerificationException);
            }
        }
        throw new SauvestAuthException(WRONG_AUTHORIZATION_HEADER,
                String.format("При попытке обновить токен произошла ошибка: в заголовок \"%s\" запроса передано некорректное значение \"%s\"",
                        HttpHeaders.AUTHORIZATION, authorizationHeader));
    }

    @Override
    public Boolean isAccountEnabled(String username) {
        try {
            UserDetails user = userService.loadUserByUsername(username);
            return user.isEnabled();
        } catch (Exception e) {
            throw new SauvestAuthException(UNKNOWN_ERROR,
                    String.format("При попытке проверить, активен ли аккаунт пользователя \"%s\", произошла ошибка: %s",
                            username, FormatUtilService.getNonNullExceptionMessage(e)),
                    e);
        }
    }

    @Override
    public Boolean isTokenValid(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret().getBytes());
        try {
            JWT.require(algorithm).build().verify(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkAccountHasRole(String username, String roleName) {
        List<Role> roles = userService.findByUsername(username).getRoles();
        for (Role role : roles) {
            if (StringUtils.equalsIgnoreCase(roleName, role.getRoleName().toString())) {
                return true;
            }
        }
        return false;
    }

    private void validateAuthenticationRequest(UsernamePasswordDto usernamePasswordDto) {
        if (StringUtils.isEmpty(usernamePasswordDto.getUsername())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке аутентифицировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передано пустое имя пользователя");
        }
        if (StringUtils.isEmpty(usernamePasswordDto.getPassword())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке аутентифицировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передан пустой пароль пользователя");
        }
    }

    private void checkIsUserEnabled(boolean isEnabled) {
        if (!isEnabled) {
            throw new SauvestAuthException(AUTHENTICATION_ERROR, "При попытке аутентифицировать пользователя произошла ошибка: " +
                    "пользователь не активен. Пожалуйста, активируйте его по высланному на указанную вами почту письму");
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
        if (StringUtils.isEmpty(userDto.getEmail())) {
            throw new SauvestAuthException(VALIDATION_ERROR, "При попытке зарегистрировать пользователя произошла ошибка валидации: " +
                    "в тело запроса передан пустая почта пользователя");
        }
    }
}
