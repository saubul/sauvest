package ru.sauvest.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleDto {

	private String open;
	private String close;
	private String low;
	private String high;
	private String volume;
	private String date;
	
}
