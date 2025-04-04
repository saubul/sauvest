package ru.sauvest.social.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.sauvest.baseservices.properties.JWTProperties;
import ru.sauvest.baseservices.properties.SauvestProperties;
import ru.sauvest.social.config.properties.MessageBrokerProperties;

@Configuration
@EnableConfigurationProperties(value = {JWTProperties.class, SauvestProperties.class, MessageBrokerProperties.class})
public class ApplicationConfiguration {

}
