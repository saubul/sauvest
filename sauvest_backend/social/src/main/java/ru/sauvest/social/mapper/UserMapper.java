package ru.sauvest.social.mapper;

import org.mapstruct.Mapper;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.UserDto;
import ru.sauvest.social.entity.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends EntityMapper<User, UserDto> {
}
