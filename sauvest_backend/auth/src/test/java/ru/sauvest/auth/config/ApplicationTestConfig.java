package ru.sauvest.auth.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.sauvest.baseservices.properties.JWTProperties;
import ru.sauvest.baseservices.properties.SauvestProperties;

@TestConfiguration
@EnableConfigurationProperties(value = {JWTProperties.class, SauvestProperties.class})
public class ApplicationTestConfig {

}
