package ru.otus.auth.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import ru.otus.baseservices.properties.JWTProperties;
import ru.otus.baseservices.properties.SauvestProperties;

@TestConfiguration
@EnableConfigurationProperties(value = {JWTProperties.class, SauvestProperties.class})
public class ApplicationTestConfig {

}
