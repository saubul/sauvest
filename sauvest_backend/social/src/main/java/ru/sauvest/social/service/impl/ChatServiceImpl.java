package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.ChatDto;
import ru.sauvest.social.dto.UserDto;
import ru.sauvest.social.entity.Chat;
import ru.sauvest.social.mapper.ChatMapper;
import ru.sauvest.social.mapper.UserMapper;
import ru.sauvest.social.repository.ChatRepository;
import ru.sauvest.social.service.ChatService;
import ru.sauvest.social.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final UserService userService;

    private final ChatMapper chatMapper;

    private final UserMapper userMapper;

    @Override
    public ChatDto findById(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);

        return chatOptional.map(chatMapper::entityToDto)
                .orElseThrow(() -> new RuntimeException(String.format("Chat with ID '%s' not found.", chatId)));
    }

    @Override
    public ChatDto findByName(String name) {
        Optional<Chat> chatOptional = chatRepository.findByName(name);

        return chatOptional.map(chatMapper::entityToDto)
                .orElseThrow(() -> new RuntimeException(String.format("Chat with Name '%s' not found.", name)));
    }

    @Override
    public List<ChatDto> findAllByUserId(Long userId) {
        return chatRepository.findByUserId(userId)
                .stream()
                .map(chatMapper::entityToDto)
                .toList();
    }

    @Override
    public List<ChatDto> findAllByUsername(String username) {
        return chatRepository.findByUsername(username)
                .stream()
                .map(chatMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public ChatDto addUser(Long chatId, Long userId) {
        UserDto user = userService.findById(userId);
        Chat chat = this.findEntityById(chatId);

        chat.getUsers().add(userMapper.dtoToEntity(user));

        chat = chatRepository.save(chat);

        return chatMapper.entityToDto(chat);
    }

    @Override
    @Transactional
    public ChatDto addUser(Long chatId, String username) {
        UserDto user = userService.findByUsername(username);
        Chat chat = this.findEntityById(chatId);

        chat.getUsers().add(userMapper.dtoToEntity(user));

        chat = chatRepository.save(chat);

        return chatMapper.entityToDto(chat);
    }

    @Override
    @Transactional
    public ChatDto save(ChatDto chatDTO) {
        Chat chat = chatRepository.save(chatMapper.dtoToEntity(chatDTO));
        return chatMapper.entityToDto(chat);
    }

    private Chat findEntityById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException(String.format("Chat with ID \"%s\" not found", chatId)));
    }

}
