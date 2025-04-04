package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sauvest.social.dto.ProfileDto;
import ru.sauvest.social.service.ProfileService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<List<ProfileDto>> getProfilesByUsernameContaining(@RequestParam("username") String username) {
        return ResponseEntity.ok(profileService.findAllByUsernameStartsWith(username));
    }

    @GetMapping("/exception")
    public ResponseEntity<List<ProfileDto>> getAllExceptUser(@RequestParam("username") String username) {
        return ResponseEntity.ok(profileService.findAllByUsernameNotIn(List.of(username)));
    }

}
