package ru.sauvest.social.kafka.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations.kafka.auth")
public class KafkaAuthProperties {

    private String groupId;

    private KafkaAuthTopicProperties topics;

    @Getter
    @Setter
    private static class KafkaAuthTopicProperties {

        private String userRegister;

    }

}
