package ru.sauvest.social.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.ChatMessageDto;
import ru.sauvest.social.entity.ChatMessage;
import ru.sauvest.social.service.ChatService;
import ru.sauvest.social.service.UserService;

@Mapper(componentModel = "spring")
public abstract class ChatMessageMapper extends EntityMapper<ChatMessage, ChatMessageDto> {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected ChatService chatService;

    @Autowired
    protected ChatMapper chatMapper;

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget ChatMessage chatMessage, ChatMessageDto chatMessageDTO) {
        chatMessage.setUser(userMapper.dtoToEntity(userService.findByUsername(chatMessageDTO.getUsername())));
        chatMessage.setChat(chatMapper.dtoToEntity(chatService.findById(chatMessageDTO.getChatId())));
    }
}
