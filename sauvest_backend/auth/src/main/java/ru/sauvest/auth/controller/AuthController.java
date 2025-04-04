package ru.sauvest.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.auth.dto.*;
import ru.sauvest.auth.service.AuthService;

@RestController
@RequestMapping("${sauvest.applicationName}/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> authenticate(@RequestBody UsernamePasswordDto usernamePasswordDto) {
        TokenResponseDto tokenResponseDTO = authService.createToken(usernamePasswordDto);
        return ResponseEntity.ok(tokenResponseDTO);
    }

    // update - существительное "обновление"
    @PostMapping("/token/update")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        TokenResponseDto tokenResponseDTO = authService.refreshToken(authorizationHeader);
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/token/validation")
    public ResponseEntity<Boolean> validateToken(@RequestBody AccessTokenDto accessTokenDto) {
        return ResponseEntity.ok(authService.isTokenValid(accessTokenDto.getToken()));
    }

    @PostMapping("/account")
    public ResponseEntity<Void> signUp(@RequestBody UserDto userDto) {
        authService.signUp(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/account/activation")
    public HttpEntity<Boolean> activateAccount(@RequestBody VerificationTokenDto verificationTokenDto) {
        return ResponseEntity.ok(authService.activateAccount(verificationTokenDto));
    }

    @PostMapping("/account/verification")
    public ResponseEntity<Boolean> checkUserEnabled(@RequestBody UsernameDto usernameDto) {
        return ResponseEntity.ok(authService.isAccountEnabled(usernameDto.getUsername()));
    }

    @PostMapping("/account/role")
    public ResponseEntity<Boolean> checkUserRole(@RequestBody UserRoleRequestDto roleDto) {
        return ResponseEntity.ok(authService.checkAccountHasRole(roleDto.getUsername(), roleDto.getRoleName()));
    }


}
