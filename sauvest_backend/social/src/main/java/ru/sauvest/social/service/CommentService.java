package ru.sauvest.social.service;

import ru.sauvest.social.dto.CommentDto;
import ru.sauvest.social.entity.Comment;

import java.util.List;

public interface CommentService {
	
	CommentDto save(CommentDto commentDTO);
	
	CommentDto save(Comment comment);

	CommentDto findById(Long commentId);

	List<CommentDto> findAllByPost(Long postId);

}
