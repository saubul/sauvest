package ru.sauvest.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sauvest.auth.dto.UserDto;
import ru.sauvest.auth.entity.User;
import ru.sauvest.baseservices.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends EntityMapper<User, UserDto> {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    public abstract UserDto entityToDto(User entity);

    @Override
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    public abstract User dtoToEntity(UserDto userDto);

}
