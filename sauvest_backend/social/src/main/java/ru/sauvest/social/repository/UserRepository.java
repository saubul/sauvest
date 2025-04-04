package ru.sauvest.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sauvest.social.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);
	
	@Query("select u from User u join Subscription s on u = s.user where u.id = :userId")
	List<User> findAllBySubscriptionUserId(Long userId);
	
	List<User> findAllBySurnameStartsWithIgnoreCase(String string);

	List<User> findAllByUsernameNotIn(List<String> usernameList);
}
