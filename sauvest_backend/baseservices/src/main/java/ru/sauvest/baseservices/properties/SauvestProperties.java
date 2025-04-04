package ru.sauvest.baseservices.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties
@RequiredArgsConstructor
public class SauvestProperties {

    private final String applicationName;

}
