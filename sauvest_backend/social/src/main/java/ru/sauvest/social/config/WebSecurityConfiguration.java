package ru.sauvest.social.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.sauvest.baseservices.filter.JWTValidationFilter;
import ru.sauvest.baseservices.properties.JWTProperties;
import ru.sauvest.baseservices.properties.SauvestProperties;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JWTProperties jwtProperties;

    private final SauvestProperties sauvestProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String applicationUrlName = "/" + sauvestProperties.getApplicationName();
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .sessionManagement(sessionManagementCustomizer ->
                        sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JWTValidationFilter(jwtProperties.getSecret()), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                applicationUrlName + "/api/v1/post",
                                applicationUrlName + "/api-docs/**",
                                applicationUrlName + "/swagger-ui/**",
                                applicationUrlName + "/monitor/**",
                                applicationUrlName + "/ws/**").permitAll()
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:4200/")); //195.133.49.174:80
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
