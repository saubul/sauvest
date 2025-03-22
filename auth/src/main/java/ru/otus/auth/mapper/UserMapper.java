package ru.otus.auth.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.auth.dto.UserDto;
import ru.otus.auth.entity.User;
import ru.otus.baseservices.mapper.EntityMapper;

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
