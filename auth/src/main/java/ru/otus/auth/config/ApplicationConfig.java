package ru.otus.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.auth.config.properties.JWTProperties;
import ru.otus.auth.config.properties.SauvestProperties;

@Configuration
@EnableConfigurationProperties(value = {JWTProperties.class, SauvestProperties.class})
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
