package ru.sauvest.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondDto {
	
	private String name;
	private String figi;
	private String ticker;
	private String classCode;
	private String isin;
	private String instrumentType;
}
