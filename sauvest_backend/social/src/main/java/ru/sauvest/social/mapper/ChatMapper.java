package ru.sauvest.social.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.ChatDto;
import ru.sauvest.social.entity.Chat;
import ru.sauvest.social.entity.User;
import ru.sauvest.social.service.UserService;

@Mapper(componentModel = "spring")
public abstract class ChatMapper extends EntityMapper<Chat, ChatDto> {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget Chat chat, ChatDto chatDTO) {
        chat.setUsers(chatDTO.getUsersUsername()
                .stream()
                .map(username -> userMapper.dtoToEntity(userService.findByUsername(username)))
                .toList()
        );
    }

    @AfterMapping
    protected void afterEntityMapping(Chat chat, @MappingTarget ChatDto chatDTO) {
        chatDTO.setUsersUsername(chat.getUsers()
                .stream()
                .map(User::getUsername)
                .toList()
        );
    }
}
