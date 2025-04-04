package ru.sauvest.social.service;

import ru.sauvest.social.dto.PostDto;
import ru.sauvest.social.entity.Post;

import java.util.List;

public interface PostService {
	
	PostDto save(PostDto postDTO);
	
	PostDto save(Post post);

	PostDto findById(Long postId);
	
	List<PostDto> findAll();
	
	List<PostDto> findAllByUsername(String username);

	List<PostDto> findByInstrumentField(String instrumentName);

	void deleteById(Long postId);

	List<PostDto> findAllByContent(String content);

}
