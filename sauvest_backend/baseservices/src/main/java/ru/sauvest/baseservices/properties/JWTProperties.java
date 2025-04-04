package ru.sauvest.baseservices.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    private final Duration expirationTime;

    private final String secret;

}
