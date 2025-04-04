package ru.sauvest.social.service;

import ru.sauvest.social.dto.ChatDto;

import java.util.List;

public interface ChatService {
	
	ChatDto findById(Long chatId);

	ChatDto findByName(String name);
	
	List<ChatDto> findAllByUserId(Long userId);
	
	List<ChatDto> findAllByUsername(String username);
	
	ChatDto addUser(Long chatId, Long userId);
	
	ChatDto addUser(Long chatId, String username);
	
	ChatDto save(ChatDto chatDTO);
	
}
