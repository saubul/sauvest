package ru.sauvest.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sauvest.social.entity.Vote;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{

	List<Vote> findByPostId(Long postId);	
	
	Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);
	
}
