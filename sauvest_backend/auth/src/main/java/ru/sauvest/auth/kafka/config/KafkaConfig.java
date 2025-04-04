package ru.sauvest.auth.kafka.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.sauvest.auth.kafka.config.properties.KafkaSocialProperties;

@Configuration
@EnableConfigurationProperties(value = KafkaSocialProperties.class)
public class KafkaConfig {

}
