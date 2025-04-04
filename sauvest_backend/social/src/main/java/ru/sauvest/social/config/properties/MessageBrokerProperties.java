package ru.sauvest.social.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "message-broker")
public class MessageBrokerProperties {

    private final String host;

    private final Integer port;

    private final String login;

    private final String password;

}
