package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.UserDto;
import ru.sauvest.social.dto.VoteDto;
import ru.sauvest.social.entity.Vote;
import ru.sauvest.social.mapper.VoteMapper;
import ru.sauvest.social.repository.VoteRepository;
import ru.sauvest.social.service.PostService;
import ru.sauvest.social.service.UserService;
import ru.sauvest.social.service.VoteService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final VoteMapper voteMapper;

    private final PostService postService;

    private final UserService userService;

    @Override
    public List<VoteDto> findAllByPostId(Long postId) {
        return voteRepository.findByPostId(postId)
                .stream()
                .map(voteMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public VoteDto save(VoteDto voteDTO) {
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndPostId(userService.findByUsername(voteDTO.getUsername()).getId(), voteDTO.getPostId());
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            voteRepository.delete(vote);
        } else {
            voteRepository.save(voteMapper.dtoToEntity(voteDTO));
        }
        return voteDTO;
    }

    @Override
    public Boolean isPostLikedByUsername(Long postId, String username) {
        UserDto user = userService.findByUsername(username);
        Optional<Vote> vote = voteRepository.findByUserIdAndPostId(user.getId(), postId);
        return vote.isPresent();
    }

}
