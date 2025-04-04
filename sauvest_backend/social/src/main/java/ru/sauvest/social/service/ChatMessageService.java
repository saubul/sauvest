package ru.sauvest.social.service;

import ru.sauvest.social.dto.ChatMessageDto;
import ru.sauvest.social.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

	List<ChatMessageDto> findAllByChatId(Long chatId);
	
	ChatMessageDto save(ChatMessage chatMessage);

	ChatMessageDto save(ChatMessageDto chatMessageDTO);
	
}
