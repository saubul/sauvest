package ru.sauvest.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstrumentAnalysisHistoryDto {

    private LocalDateTime date;

    private String username;

    private String figi;

    private Float recommendation;

    private Float ema;

    private Float rsi;

    private Float macd;

    private Float stochastic;

    private Float sar;

    private Date dateAnalysis;

}
