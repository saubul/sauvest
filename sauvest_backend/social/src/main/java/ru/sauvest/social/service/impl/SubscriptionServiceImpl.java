package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.ProfileDto;
import ru.sauvest.social.dto.SubscriptionDto;
import ru.sauvest.social.entity.Subscription;
import ru.sauvest.social.mapper.ProfileMapper;
import ru.sauvest.social.mapper.SubscriptionMapper;
import ru.sauvest.social.mapper.UserMapper;
import ru.sauvest.social.repository.SubscriptionRepository;
import ru.sauvest.social.service.SubscriptionService;
import ru.sauvest.social.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;

    private final UserService userService;

    private final UserMapper userMapper;

    private final ProfileMapper profileMapper;

    @Override
    public SubscriptionDto save(SubscriptionDto subscriptionDTO) {
        return this.save(subscriptionMapper.dtoToEntity(subscriptionDTO));
    }

    @Override
    public SubscriptionDto save(Subscription subscription) {
        return subscriptionMapper.entityToDto(this.subscriptionRepository.save(subscription));
    }

    @Override
    public List<SubscriptionDto> findAllByUsername(String username) {
        return subscriptionRepository.findAllByUserId(userService.findByUsername(username).getId())
                .stream()
                .map(subscriptionMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public SubscriptionDto delete(SubscriptionDto subscriptionDTO) {
        subscriptionRepository.deleteByUserIdAndSubscriptionUserId(
                userService.findByUsername(subscriptionDTO.getUsername()).getId(),
                userService.findByUsername(subscriptionDTO.getSubUsername()).getId()
        );
        return subscriptionDTO;
    }

    @Override
    public Boolean checkIsSubscribed(SubscriptionDto subscriptionDTO) {
        Optional<Subscription> subscriptionOptional = subscriptionRepository
                .findByUserIdAndSubscriptionUserId(
                        userService.findByUsername(subscriptionDTO.getUsername()).getId(),
                        userService.findByUsername(subscriptionDTO.getSubUsername()).getId()
                );
        return subscriptionOptional.isPresent();
    }

    @Override
    public List<ProfileDto> findAllUsersSubscriptionsByUsername(String username) {
        List<SubscriptionDto> subscriptionDtos = this.findAllByUsername(username);
        return subscriptionDtos.stream()
                .map(subscriptionDto -> profileMapper.entityToDto(
                        userMapper.dtoToEntity(userService.findByUsername(subscriptionDto.getSubUsername())))
                )
                .sorted(Comparator.comparing(ProfileDto::getSurname))
                .toList();
    }

}
