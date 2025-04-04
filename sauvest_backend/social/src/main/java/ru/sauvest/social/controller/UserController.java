package ru.sauvest.social.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.UserDto;
import ru.sauvest.social.service.ChatService;
import ru.sauvest.social.service.UserService;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<UserDto> getUser(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDTO) {
        return ResponseEntity.ok(userService.update(userDTO));
    }

}
