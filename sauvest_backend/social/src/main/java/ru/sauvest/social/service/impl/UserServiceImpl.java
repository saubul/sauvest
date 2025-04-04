package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.sauvest.baseservices.exception.SauvestException;
import ru.sauvest.social.dto.UserDto;
import ru.sauvest.social.entity.User;
import ru.sauvest.social.mapper.UserMapper;
import ru.sauvest.social.repository.UserRepository;
import ru.sauvest.social.service.UserService;

import java.util.Optional;

import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto save(User user) {
        return userMapper.entityToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDTO) {
        return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDTO)));
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(userMapper::entityToDto)
                .orElseThrow(() -> new SauvestException(
                        RESOURCE_NOT_FOUND,
                        String.format("Пользователь с логиноим \"%s\" не найден", username))
                );
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(userMapper::entityToDto)
                .orElseThrow(() -> new RuntimeException(String.format("User with id '%s' not found.", id)));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        UserDto user = findByUsername(userDto.getUsername());
        if (!StringUtils.equals(userDto.getName(), user.getName())) {
            user.setName(userDto.getName());
        }
        if (!StringUtils.equals(userDto.getSurname(), user.getSurname())) {
            user.setSurname(userDto.getSurname());
        }
        if (!StringUtils.equals(userDto.getSsoToken(), user.getSsoToken())) {
            user.setSsoToken(userDto.getSsoToken());
        }
        save(user);
        return userDto;
    }
}
