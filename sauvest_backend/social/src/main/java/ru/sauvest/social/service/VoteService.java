package ru.sauvest.social.service;

import ru.sauvest.social.dto.VoteDto;

import java.util.List;

public interface VoteService {

    List<VoteDto> findAllByPostId(Long postId);

    VoteDto save(VoteDto voteDTO);

    Boolean isPostLikedByUsername(Long postId, String username);

}
