package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.sauvest.social.dto.ChatMessageDto;
import ru.sauvest.social.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
	
	private final ChatMessageService chatMessageService;

	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/chat/{chatId}")
	public void sendChatMessage(@DestinationVariable Long chatId, ChatMessageDto chatMessageDto) {
		chatMessageService.save(chatMessageDto);
		simpMessagingTemplate.convertAndSend("/topic/" + chatId, chatMessageDto);
	}
	
}
