package ru.sauvest.social.service;

import ru.sauvest.social.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    List<ProfileDto> findAllByUsernameStartsWith(String string);

    List<ProfileDto> findAllByUsernameNotIn(List<String> usernameList);

}
