package ru.sauvest.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetInstrumentInfoRequestDto {

    private String ssoToken;

    private String field;

}
