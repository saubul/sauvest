package ru.sauvest.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sauvest.auth.dto.UserDto;
import ru.sauvest.auth.entity.User;
import ru.sauvest.auth.mapper.UserMapper;
import ru.sauvest.auth.repository.UserRepository;
import ru.sauvest.baseservices.exception.SauvestException;
import ru.sauvest.baseservices.service.FormatUtilService;

import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.RESOURCE_NOT_FOUND;
import static ru.sauvest.baseservices.exception.ExceptionCodeConstants.UNKNOWN_ERROR;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("При попытке загрузить пользователя произошла ошибка: не удалось найти пользователя по логину \"%s\"", username)
                )
        );
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        try {
            return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDto)));
        } catch (Exception e) {
            throw new SauvestException(UNKNOWN_ERROR, String.format("При попытке сохранить пользователя произошла ошибка: %s",
                    FormatUtilService.getNonNullExceptionMessage(e)));
        }
    }

    @Transactional
    public UserDto save(User user) {
        try {
            return userMapper.entityToDto(userRepository.save(user));
        } catch (Exception e) {
            throw new SauvestException(UNKNOWN_ERROR, String.format("При попытке сохранить пользователя произошла ошибка: %s",
                    FormatUtilService.getNonNullExceptionMessage(e)));
        }
    }

    protected User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new SauvestException(RESOURCE_NOT_FOUND,
                        String.format("При попытке загрузить пользователя произошла ошибка: не удалось найти пользователя по логину \"%s\"", username)
                )
        );
    }

}
