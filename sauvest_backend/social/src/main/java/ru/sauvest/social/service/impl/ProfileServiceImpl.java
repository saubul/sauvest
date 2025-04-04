package ru.sauvest.social.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.ProfileDto;
import ru.sauvest.social.mapper.ProfileMapper;
import ru.sauvest.social.repository.UserRepository;
import ru.sauvest.social.service.ProfileService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final ProfileMapper profileMapper;

    @Override
    public List<ProfileDto> findAllByUsernameStartsWith(String string) {
        return userRepository.findAllBySurnameStartsWithIgnoreCase(string)
                .stream()
                .map(profileMapper::entityToDto)
                .toList();
    }

    @Override
    public List<ProfileDto> findAllByUsernameNotIn(List<String> usernameList) {
        return userRepository.findAllByUsernameNotIn(usernameList)
                .stream()
                .map(profileMapper::entityToDto)
                .toList();
    }

}
