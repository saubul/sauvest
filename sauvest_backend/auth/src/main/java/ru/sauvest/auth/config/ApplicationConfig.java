package ru.sauvest.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sauvest.baseservices.config.GrpcConfig;
import ru.sauvest.baseservices.properties.JWTProperties;
import ru.sauvest.baseservices.properties.SauvestProperties;

@Configuration
@Import(GrpcConfig.class)
@EnableConfigurationProperties(value = {JWTProperties.class, SauvestProperties.class})
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
