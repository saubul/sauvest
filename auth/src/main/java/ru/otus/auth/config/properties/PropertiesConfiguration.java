package ru.otus.auth.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {JWTProperties.class})
public class PropertiesConfiguration {
}
