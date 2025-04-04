package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.InstrumentDto;
import ru.sauvest.social.dto.PostDto;
import ru.sauvest.social.service.InstrumentService;
import ru.sauvest.social.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/instrument")
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;

    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<List<InstrumentDto>> getInstrumentsByPost(@RequestParam("postId") Long postId) {
        return ResponseEntity.ok(this.instrumentService.findByPost(postId));
    }

    @GetMapping("/field")
    public ResponseEntity<List<PostDto>> getPostsByInstrument(@PathVariable("field") String field) {
        return ResponseEntity.ok(this.postService.findByInstrumentField(field));
    }

}
