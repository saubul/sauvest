package ru.sauvest.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
	
	private Long id;

	private String username;

	private String content;

	private String imgPath;

	private LocalDateTime creationDateTime;

	private Integer voteCount;

	private String name;

	private String surname;

	private List<InstrumentDto> instruments;
	
}
