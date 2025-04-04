package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.PostDto;
import ru.sauvest.social.entity.Post;
import ru.sauvest.social.entity.User;
import ru.sauvest.social.mapper.PostMapper;
import ru.sauvest.social.mapper.UserMapper;
import ru.sauvest.social.repository.PostRepository;
import ru.sauvest.social.service.PostService;
import ru.sauvest.social.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public PostDto findById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        return postOptional.map(postMapper::entityToDto)
                .orElseThrow(() -> new RuntimeException(String.format("Post with id '%s' not found.", postId)));
    }

    @Override
    public List<PostDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::entityToDto)
                .sorted((postDto1, postDto2) -> postDto2.getCreationDateTime().compareTo(postDto1.getCreationDateTime()))
                .toList();
    }

    @Override
    @Transactional
    public PostDto save(PostDto postDTO) {
        Post post = postRepository.save(postMapper.dtoToEntity(postDTO));
        return postMapper.entityToDto(post);
    }

    @Override
    @Transactional
    public PostDto save(Post post) {
        return postMapper.entityToDto(postRepository.save(post));
    }

    @Override
    public List<PostDto> findAllByUsername(String username) {
        User user = userMapper.dtoToEntity(userService.findByUsername(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::entityToDto)
                .sorted((postDto1, postDto2) -> postDto2.getCreationDateTime().compareTo(postDto1.getCreationDateTime()))
                .toList();
    }

    @Override
    public List<PostDto> findByInstrumentField(String instrumentField) {
        return postRepository.findAll()
                .stream()
                .filter(post -> post.getInstruments()
                        .stream()
                        .anyMatch(instrument -> instrumentField.equals(instrument.getTicker()) ||
                                instrumentField.equals(instrument.getFigi())))
                .map(postMapper::entityToDto)
                .sorted((postDto1, postDto2) -> postDto2.getCreationDateTime().compareTo(postDto1.getCreationDateTime()))
                .toList();
    }

    @Override
    public List<PostDto> findAllByContent(String content) {
        return postRepository.findByContentContainingIgnoreCase(content).stream()
                .map(postMapper::entityToDto)
                .sorted((postDto1, postDto2) -> postDto2.getCreationDateTime().compareTo(postDto1.getCreationDateTime()))
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }

}
