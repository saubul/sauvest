package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.ChatMessageDto;
import ru.sauvest.social.entity.ChatMessage;
import ru.sauvest.social.mapper.ChatMessageMapper;
import ru.sauvest.social.repository.ChatMessageRepository;
import ru.sauvest.social.service.ChatMessageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessageDto> findAllByChatId(Long chatId) {
        return chatMessageRepository.findAllByChatId(chatId)
                .stream()
                .map(chatMessageMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatMessageDto save(ChatMessage chatMessage) {
        return chatMessageMapper.entityToDto(chatMessageRepository.save(chatMessage));
    }

    @Override
    @Transactional
    public ChatMessageDto save(ChatMessageDto chatMessageDTO) {
        ChatMessage chatMessage = chatMessageMapper.dtoToEntity(chatMessageDTO);
        chatMessageRepository.save(chatMessage);
        return chatMessageDTO;
    }

}
