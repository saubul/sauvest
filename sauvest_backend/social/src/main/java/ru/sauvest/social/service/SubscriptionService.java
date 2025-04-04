package ru.sauvest.social.service;

import ru.sauvest.social.dto.ProfileDto;
import ru.sauvest.social.dto.SubscriptionDto;
import ru.sauvest.social.entity.Subscription;

import java.util.List;

public interface SubscriptionService {
	
	SubscriptionDto save(SubscriptionDto subscriptionDTO);
	
	SubscriptionDto save(Subscription subscription);

	List<SubscriptionDto> findAllByUsername(String username);

	Boolean checkIsSubscribed(SubscriptionDto subscriptionDTO);

	SubscriptionDto delete(SubscriptionDto subscriptionDTO);

	List<ProfileDto> findAllUsersSubscriptionsByUsername(String username);
	
}
