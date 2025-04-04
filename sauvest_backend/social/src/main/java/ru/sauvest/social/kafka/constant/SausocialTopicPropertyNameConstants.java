package ru.sauvest.social.kafka.constant;

import lombok.Getter;

@Getter
public final class SausocialTopicPropertyNameConstants {

    private SausocialTopicPropertyNameConstants() {}

    private static final String INTEGRATIONS_KAFKA_PREFIX = "integrations.kafka.";

    private static final String AUTH_APPLICATION_PREFIX = "auth.topics.";

    public static final String USER_REGISTER_EVENT_TOPIC = INTEGRATIONS_KAFKA_PREFIX + AUTH_APPLICATION_PREFIX + "user-register";

}
