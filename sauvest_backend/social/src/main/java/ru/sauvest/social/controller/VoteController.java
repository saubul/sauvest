package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.VoteDto;
import ru.sauvest.social.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @GetMapping
    public ResponseEntity<Boolean> isPostLikedByUser(@RequestParam("postId") Long postId, @RequestParam("username") String username) {
        return ResponseEntity.ok(voteService.isPostLikedByUsername(postId, username));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<VoteDto>> getVotesByPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(voteService.findAllByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<VoteDto> vote(@RequestBody VoteDto voteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voteService.save(voteDTO));
    }

}
