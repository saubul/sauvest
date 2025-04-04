package ru.sauvest.auth.kafka.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations.kafka.social")
public class KafkaSocialProperties {

    private KafkaSocialTopicProperties topics;

    @Getter
    @Setter
    public static class KafkaSocialTopicProperties {

        private String userRegister;

    }

}