package ru.sauvest.social.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.sauvest.baseservices.properties.SauvestProperties;
import ru.sauvest.social.config.properties.MessageBrokerProperties;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	private final SauvestProperties sauvestProperties;

	private final MessageBrokerProperties messageBrokerProperties;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		String applicationUrlName = "/" + sauvestProperties.getApplicationName();
		registry.addEndpoint(applicationUrlName + "/ws")
				.setAllowedOrigins("http://localhost:4200", "http://localhost:4200/")
				.withSockJS(); //195.133.49.174:80
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableStompBrokerRelay("/topic", "/queue")
				.setRelayHost(messageBrokerProperties.getHost())
				.setRelayPort(messageBrokerProperties.getPort())
				.setClientLogin(messageBrokerProperties.getLogin())
				.setClientPasscode(messageBrokerProperties.getPassword())
				.setSystemLogin(messageBrokerProperties.getLogin())
				.setSystemPasscode(messageBrokerProperties.getPassword());
	}
	
}
