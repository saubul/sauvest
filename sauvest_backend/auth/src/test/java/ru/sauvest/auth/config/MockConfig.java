package ru.sauvest.auth.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MockConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

}
