package ru.sauvest.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sauvest.social.entity.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{

	List<Subscription> findAllByUserId(Long userId);
	
	Optional<Subscription> findByUserIdAndSubscriptionUserId(Long userId, Long subscriptionUserId);
	
	void deleteByUserIdAndSubscriptionUserId(Long userId, Long subscriptionUserId);
	
}
