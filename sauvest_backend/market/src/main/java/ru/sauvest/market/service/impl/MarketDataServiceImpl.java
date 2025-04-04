package ru.sauvest.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.market.service.MarketDataService;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MarketDataServiceImpl implements MarketDataService {

    public CompletableFuture<List<HistoricCandle>> getInstrumentMarketData(String ssoToken, String figi, CandleInterval candleInterval) {
        InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
        ru.tinkoff.piapi.core.MarketDataService marketDataService = investApi.getMarketDataService();
        Instant fromDate = Instant.now();
        if (candleInterval == CandleInterval.CANDLE_INTERVAL_DAY) {
            fromDate = fromDate.minus(364, ChronoUnit.DAYS);
        } else if (candleInterval == CandleInterval.CANDLE_INTERVAL_HOUR) {
            fromDate = fromDate.minus(21, ChronoUnit.DAYS);
        } else if (candleInterval == CandleInterval.CANDLE_INTERVAL_4_HOUR) {
            fromDate = fromDate.minus(21, ChronoUnit.DAYS);
        } else if (candleInterval == CandleInterval.CANDLE_INTERVAL_5_MIN) {
            fromDate = fromDate.minus(2, ChronoUnit.DAYS);
        }
        return marketDataService.getCandles(figi, fromDate, Instant.now(), candleInterval);
    }

    public List<HistoricCandle> getInstrumentMarketDataSync(String ssoToken, String figi, CandleInterval candleInterval) {
        InvestApi investApi = InvestApiUtilService.getInvestApi(ssoToken);
        ru.tinkoff.piapi.core.MarketDataService marketDataService = investApi.getMarketDataService();
        Instant fromDate = Instant.now();
        if (candleInterval == CandleInterval.CANDLE_INTERVAL_DAY) {
            fromDate = fromDate.minus(364, ChronoUnit.DAYS);
        } else if (candleInterval == CandleInterval.CANDLE_INTERVAL_HOUR) {
            fromDate = fromDate.minus(14, ChronoUnit.DAYS);
        } else if (candleInterval == CandleInterval.CANDLE_INTERVAL_4_HOUR) {
            fromDate = fromDate.minus(21, ChronoUnit.DAYS);
        } else if (candleInterval == CandleInterval.CANDLE_INTERVAL_5_MIN) {
            fromDate = fromDate.minus(2, ChronoUnit.DAYS);
        }
        return marketDataService.getCandlesSync(figi, fromDate, Instant.now(), candleInterval);
    }

}
