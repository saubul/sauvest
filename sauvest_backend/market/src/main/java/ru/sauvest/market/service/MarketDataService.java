package ru.sauvest.market.service;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MarketDataService {
	
	CompletableFuture<List<HistoricCandle>> getInstrumentMarketData(String username, String figi, CandleInterval candleInterval);
	
	List<HistoricCandle> getInstrumentMarketDataSync(String username, String figi, CandleInterval candleInterval);

}
