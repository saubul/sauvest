package ru.sauvest.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.stream.Collectors;

public final class JwtUtilService {

    private JwtUtilService() {
    }

    public static String getJwtToken(UserDetails userDetails, Date tokenExpiresDate, Algorithm algorithm) {
        return JWT.create()
                .withExpiresAt(tokenExpiresDate)
                .withClaim("username", userDetails.getUsername())
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

}
