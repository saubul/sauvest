package ru.sauvest.social.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllPostsByContentRequestDto {

    private String content;

}
