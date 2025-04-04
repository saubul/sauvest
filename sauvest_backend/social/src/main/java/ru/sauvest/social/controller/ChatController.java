package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.ChatDto;
import ru.sauvest.social.dto.ChatMessageDto;
import ru.sauvest.social.service.ChatMessageService;
import ru.sauvest.social.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ChatDto> createChat(@RequestBody ChatDto chatDTO) {
        return new ResponseEntity<>(chatService.save(chatDTO), HttpStatus.OK);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable("chatId") Long chatId) {
        return new ResponseEntity<>(chatMessageService.findAllByChatId(chatId), HttpStatus.OK);
    }

    @GetMapping("/{username}/chats")
    public ResponseEntity<List<ChatDto>> getChatsByUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(chatService.findAllByUsername(username));
    }

}
