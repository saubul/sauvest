package ru.sauvest.social.service;


import ru.sauvest.social.dto.UserDto;
import ru.sauvest.social.entity.User;

public interface UserService {

    UserDto save(User user);

    UserDto save(UserDto userDTO);

    UserDto findByUsername(String username);

    UserDto findById(Long id);

    UserDto update(UserDto userDTO);
}
