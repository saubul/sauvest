package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.ProfileDto;
import ru.sauvest.social.dto.SubscriptionDto;
import ru.sauvest.social.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<Boolean> isSubscribed(@RequestParam("username") String username,
                                                @RequestParam("subUsername") String subUsername) {
        SubscriptionDto subscriptionDto = SubscriptionDto.builder()
                .username(username)
                .subUsername(subUsername)
                .build();
        return ResponseEntity.ok(subscriptionService.checkIsSubscribed(subscriptionDto));
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> subscribe(@RequestBody SubscriptionDto subscriptionDto) {
        return ResponseEntity.ok(subscriptionService.save(subscriptionDto));
    }

    @DeleteMapping
    public ResponseEntity<SubscriptionDto> unsubscribe(@RequestParam("username") String username,
                                                       @RequestParam("subUsername") String subUsername) {
        SubscriptionDto subscriptionDto = SubscriptionDto.builder()
                .username(username)
                .subUsername(subUsername)
                .build();
        return ResponseEntity.ok(subscriptionService.delete(subscriptionDto));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ProfileDto>> getAllSubscriptionsByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(subscriptionService.findAllUsersSubscriptionsByUsername(username));
    }

}
