package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.CommentDto;
import ru.sauvest.social.dto.GetAllPostsByContentRequestDto;
import ru.sauvest.social.dto.PostDto;
import ru.sauvest.social.service.CommentService;
import ru.sauvest.social.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }

    @PostMapping
    public ResponseEntity<PostDto> savePost(@RequestBody PostDto postDto) {
        PostDto result = postService.save(postDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable("postId") Long postId) {
        postService.deleteById(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // TODO Pageable
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDto>> getAllPostsByUsername(@RequestParam(value = "username") String username) {
        return ResponseEntity.ok(postService.findAllByUsername(username));
    }

    @GetMapping("/instrument")
    public ResponseEntity<List<PostDto>> getAllPostsByInstrument(@RequestParam(value = "instrument") String instrument) {
        return ResponseEntity.ok(postService.findByInstrumentField(instrument));
    }

    @PostMapping("/content")
    public ResponseEntity<List<PostDto>> getAllPostsByContent(@RequestBody GetAllPostsByContentRequestDto requestDto) {
        return ResponseEntity.ok(postService.findAllByContent(requestDto.getContent()));
    }

}
