package ru.sauvest.baseservices.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JWTValidationFilter extends OncePerRequestFilter {

    private final String secret;

    public JWTValidationFilter(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.contains("/ws") ||
                path.contains("/api-docs") ||
                path.contains("/monitor") ||
                path.contains("/swagger-ui")) {
            doFilter(request, response, filterChain);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String accessToken = authorizationHeader.substring("Bearer ".length());

                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(this.secret.getBytes())).build();

                    DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);

                    String username = decodedJWT.getClaim("username").asString();

                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
                    UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(username, null, authorities);

                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(securityContext);

                    doFilter(request, response, filterChain);

                } catch (Exception e) {
                    response.sendError(403, e.getMessage());
                }
            } else {
                response.sendError(401, "Wrong authorization token (trying to verify token).");
            }
        }
    }

}
