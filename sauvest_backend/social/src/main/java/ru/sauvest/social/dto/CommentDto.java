package ru.sauvest.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long id;

    private Long postId;

    private String username;

    private String content;

    private LocalDateTime creationDateTime;

}
