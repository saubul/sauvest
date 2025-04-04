package ru.sauvest.market.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.sauvest.baseservices.properties.JWTProperties;
import ru.sauvest.baseservices.properties.SauvestProperties;

@Configuration
@EnableConfigurationProperties(value = {JWTProperties.class, SauvestProperties.class})
public class ApplicationConfiguration {

}
