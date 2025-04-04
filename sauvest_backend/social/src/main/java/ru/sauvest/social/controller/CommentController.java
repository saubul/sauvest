package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.CommentDto;
import ru.sauvest.social.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDTO) {
        return ResponseEntity.ok(commentService.save(commentDTO));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findAllByPost(postId));
    }

}
