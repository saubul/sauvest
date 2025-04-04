package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.CommentDto;
import ru.sauvest.social.entity.Comment;
import ru.sauvest.social.mapper.CommentMapper;
import ru.sauvest.social.repository.CommentRepository;
import ru.sauvest.social.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final CommentRepository commentRepository;

	private final CommentMapper commentMapper;

	@Override
	public CommentDto findById(Long commentId) {
		Optional<Comment> commentOptional = commentRepository.findById(commentId);

        return commentOptional.map(commentMapper::entityToDto)
				.orElseThrow(() -> new RuntimeException(String.format("Comment with id '%s' not found.", commentId)));
	}
	
	@Override
	public List<CommentDto> findAllByPost(Long postId) {
		return commentRepository.findAllByPostId(postId)
				.stream()
				.map(commentMapper::entityToDto)
				.toList();
	}

	@Override
	@Transactional
	public CommentDto save(CommentDto commentDTO) {
		Comment comment = commentRepository.save(commentMapper.dtoToEntity(commentDTO));
		return commentMapper.entityToDto(comment);
	}

	@Override
	@Transactional
	public CommentDto save(Comment comment) {
		return this.save(commentMapper.entityToDto(comment));
	}
	
}
