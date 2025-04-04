package ru.sauvest.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import ru.sauvest.market.dto.CandleDto;
import ru.sauvest.market.dto.GetInstrumentDataRequestDto;
import ru.sauvest.market.service.MarketDataService;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/marketData")
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataService marketDataService;

    // TODO WebFlux??
    @GetMapping(value = "/instrumentData", produces = "text/event-stream")
    public SseEmitter getInstrumentMarketData(@RequestParam("figi") String figi,
                                              @RequestParam("interval") String interval,
                                              @RequestHeader("SsoToken") String ssoToken) {
        CompletableFuture<List<HistoricCandle>> candles = marketDataService.getInstrumentMarketData(ssoToken, figi, CandleInterval.valueOf(interval));
        SseEmitter sseEmitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            candles.whenComplete((res, ex) -> {
                SseEventBuilder event = SseEmitter.event()
                        .data(res.stream().map(this::convertCandle).collect(Collectors.toList()))
                        .name("message");
                try {
                    sseEmitter.send(event);
                } catch (IOException e) {
                    sseEmitter.completeWithError(ex);
                }
            });
        });

        return sseEmitter;
    }

    @PostMapping(value = "/instrumentDataSync")
    public List<CandleDto> getInstrumentMarketDataSync(@RequestBody GetInstrumentDataRequestDto requestDto) {
        List<HistoricCandle> candles = marketDataService.getInstrumentMarketDataSync(requestDto.getSsoToken(),
                requestDto.getFigi(), CandleInterval.valueOf(requestDto.getInterval()));
        return candles.stream().map(this::convertCandle).toList();
    }

    private CandleDto convertCandle(HistoricCandle historicCandle) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dateFormat.format(new Date(historicCandle.getTime().getSeconds() * 1000));
        return CandleDto.builder()
                .open(convertQuotation(historicCandle.getOpen()))
                .close(convertQuotation(historicCandle.getClose()))
                .low(convertQuotation(historicCandle.getLow()))
                .high(convertQuotation(historicCandle.getHigh()))
                .volume(String.valueOf(historicCandle.getVolume()))
                .date(date)
                .build();
    }

    private String convertQuotation(Quotation quotation) {
        return quotation.getUnits() + "." + quotation.getNano();
    }

}
