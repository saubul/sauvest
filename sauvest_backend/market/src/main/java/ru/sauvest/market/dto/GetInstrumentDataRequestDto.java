package ru.sauvest.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetInstrumentDataRequestDto {

    private String ssoToken;

    private String figi;

    private String interval;

}
