package ru.sauvest.social.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.SubscriptionDto;
import ru.sauvest.social.entity.Subscription;
import ru.sauvest.social.service.UserService;

@Mapper(componentModel = "spring")
public abstract class SubscriptionMapper extends EntityMapper<Subscription, SubscriptionDto> {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget Subscription subscription, SubscriptionDto subscriptionDto) {
        subscription.setUser(userMapper.dtoToEntity(userService.findByUsername(subscriptionDto.getUsername())));
        subscription.setSubscriptionUser(userMapper.dtoToEntity(userService.findByUsername(subscriptionDto.getSubUsername())));
    }

    @AfterMapping
    protected void afterEntityMapping(Subscription subscription, @MappingTarget SubscriptionDto subscriptionDto) {
        subscriptionDto.setUsername(subscription.getUser().getUsername());
        subscriptionDto.setSubUsername(subscription.getSubscriptionUser().getUsername());
    }

}
