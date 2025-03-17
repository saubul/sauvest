package ru.otus.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.auth.dto.UserDto;
import ru.otus.auth.entity.User;
import ru.otus.auth.mapper.UserMapper;
import ru.otus.auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("При попытке загрузить пользователя произошла ошибка: не удалось найти пользователя по логину \"%s\"", username))
        );
    }

    @Transactional
    public User save(UserDto userDto) {
        return userRepository.save(userMapper.dtoToEntity(userDto));
    }

}
