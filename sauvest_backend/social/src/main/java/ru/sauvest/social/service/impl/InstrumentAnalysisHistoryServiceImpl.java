package ru.sauvest.social.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.CandleDto;
import ru.sauvest.social.dto.InstrumentAnalysisHistoryDto;
import ru.sauvest.social.entity.InstrumentAnalysisHistory;
import ru.sauvest.social.mapper.InstrumentAnalysisHistoryMapper;
import ru.sauvest.social.repository.InstrumentAnalysisHistoryRepository;
import ru.sauvest.social.service.InstrumentAnalysisHistoryService;
import ru.sauvest.social.service.InstrumentService;
import ru.sauvest.social.service.UserService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentAnalysisHistoryServiceImpl implements InstrumentAnalysisHistoryService {

    private final InstrumentAnalysisHistoryRepository instrumentAnalysisHistoryRepository;

    private final InstrumentAnalysisHistoryMapper instrumentAnalysisHistoryMapper;

    private final UserService userService;

    private final InstrumentService instrumentService;

    @Override
    public List<InstrumentAnalysisHistoryDto> findByUsernameAndFigi(String username, String figi) {
        List<InstrumentAnalysisHistory> instrumentAnalysisHistoryList = instrumentAnalysisHistoryRepository.findAllByUsername(username);
        return instrumentAnalysisHistoryList.stream()
                .map(instrumentAnalysisHistoryMapper::entityToDto)
                .sorted((history1, history2) -> history2.getDate().compareTo(history1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public InstrumentAnalysisHistoryDto save(InstrumentAnalysisHistoryDto instrumentAnalysisHistoryDTO) {
        return instrumentAnalysisHistoryMapper.entityToDto(
                instrumentAnalysisHistoryRepository.save(instrumentAnalysisHistoryMapper.dtoToEntity(instrumentAnalysisHistoryDTO))
        );
    }

    @Override
    public InstrumentAnalysisHistoryDto executeTechnicalAnalysis(List<CandleDto> candleDtoList,
                                                                 Date date, String username, String figi) {
        CandleDto currentCandleDto = UtilService.getCandleOnDate(candleDtoList, date);
        Float ema = this.getEma(candleDtoList, date, 31);
        Float rsi = this.getRsi(candleDtoList, date, 31);
        Float macd = this.getMacd(candleDtoList, date);
        Float stochastic = this.getStochastic(candleDtoList, date, 31);
        Float sar = this.getSar(candleDtoList, date, 31);
        InstrumentAnalysisHistoryDto instrumentAnalysisHistoryDTO = InstrumentAnalysisHistoryDto.builder()
                .dateAnalysis(date)
                .ema(ema)
                .rsi(rsi)
                .macd(macd)
                .stochastic(stochastic)
                .sar(sar)
                .recommendation(this.getRecommendation(currentCandleDto, ema, rsi, macd, stochastic, sar))
                .username(username)
                .figi(figi)
                .build();
        InstrumentAnalysisHistoryDto instrumentAnalysisHistory = this.save(instrumentAnalysisHistoryDTO);
        return instrumentAnalysisHistory;
    }

    private Float getRecommendation(CandleDto candleDTO, float ema, float rsi, float macd, float stochastic, float sar) {
        float closePrice = Float.parseFloat(candleDTO.getClose());
        //ema
        float emaDiff = (closePrice - ema) / closePrice;
        float emaFactor = 0f;
        if (emaDiff > 0.1) {
            emaFactor = 1;
        } else if (emaDiff < -0.1) {
            emaFactor = -1;
        }
        //rsi
        float rsiDiff = (50 - rsi) / 50;
        float rsiFactor = 0f;
        if (rsiFactor > 0.3) {
            rsiFactor = 1;
        } else if (rsiFactor < -0.3) {
            rsiFactor = -1;
        }

        //macd
        float macdDiff = macd / ema;
        float macdFactor = 0f;
        if (macdDiff > 0) {

        }

        float stochasticDiff = (50 - stochastic) / 50;
        float stochasticFactor = 0f;
        if (stochasticDiff > 0.3) {
            stochasticFactor = 1;
        } else if (stochasticFactor < -0.3) {
            stochasticFactor = -1;
        }

        float sarDiff = (closePrice - sar) / closePrice;
        float sarFactor = 0f;
        if (Math.abs(sarDiff) > 0.3) {
            stochasticFactor = 1;
        } else if (Math.abs(sarDiff) < 0.1) {
            stochasticFactor = -1;
        }

        // TODO
        float resFactor = emaFactor / 2 + rsiFactor / 2;
        if (resFactor > 0.4) {
            return 1f;
        } else if (resFactor < -0.4) {
            return -1f;
        } else {
            return 0f;
        }
    }

    private Float getStochastic(List<CandleDto> candleDtoList, Date date, int period) {
        LocalDate dateEnd = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateBegin = dateEnd.minusDays(period);
        List<CandleDto> candles = candleDtoList.stream().filter(candleDTO -> {
            LocalDate candleDate = UtilService.convertCandleDateToLocalDate(candleDTO);
            return candleDate.isAfter(dateBegin) && candleDate.isBefore(dateEnd);
        }).toList();

        CandleDto currCandle = UtilService.getCandleOnDate(candleDtoList, date);
        Float low = Float.parseFloat(currCandle.getLow());
        Float high = Float.parseFloat(currCandle.getHigh());
        for (CandleDto candleDTO : candleDtoList) {
            if (low > Float.parseFloat(candleDTO.getLow())) {
                low = Float.parseFloat(candleDTO.getLow());
            }
            if (high < Float.parseFloat(candleDTO.getHigh())) {
                high = Float.parseFloat(candleDTO.getHigh());
            }
        }
        Float stochastic = ((Float.parseFloat(currCandle.getClose()) - low) / (high - low)) * 100;

        return stochastic;
    }

    private Float getSar(List<CandleDto> candleDtoList, Date date, int period) {
        LocalDate dateEnd = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateBegin = dateEnd.minusDays(period);
        List<CandleDto> candles = candleDtoList.stream().filter(candleDTO -> {
            LocalDate candleDate = UtilService.convertCandleDateToLocalDate(candleDTO);
            return candleDate.isAfter(dateBegin) && candleDate.isBefore(dateEnd);
        }).toList();
        float multiplier = 0.1f;
        float[] sar = new float[candles.size()];
        sar[0] = Float.parseFloat(candles.get(0).getClose());
        Float high = Float.parseFloat(candles.get(0).getHigh());
        for (CandleDto candleDTO : candles) {
            if (high < Float.parseFloat(candleDTO.getHigh())) {
                high = Float.parseFloat(candleDTO.getHigh());
            }
        }
        for (int i = 0; i < sar.length - 1; i++) {
            sar[i + 1] = sar[i] + multiplier * (high - Float.parseFloat(candles.get(i + 1).getClose()));
        }
        return sar[sar.length - 1];
    }

    private Float getMacd(List<CandleDto> candleDtoList, Date date) {
        return getEma(candleDtoList, date, 12) - getEma(candleDtoList, date, 26);
    }

    private Float getEma(List<CandleDto> candleDtoList, Date date, int period) {
        LocalDate dateEnd = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateBegin = dateEnd.minusDays(period);
        List<CandleDto> candles = candleDtoList.stream().filter(candleDTO -> {
            LocalDate candleDate = UtilService.convertCandleDateToLocalDate(candleDTO);
            return candleDate.isAfter(dateBegin) && candleDate.isBefore(dateEnd);
        }).toList();

        return ema(candles, period);
    }

    private Float getRsi(List<CandleDto> candleDtoList, Date date, int period) {
        LocalDate dateEnd = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateBegin = dateEnd.minusDays(period);
        List<CandleDto> candles = candleDtoList.stream().filter(candleDTO -> {
            LocalDate candleDate = UtilService.convertCandleDateToLocalDate(candleDTO);
            return candleDate.isAfter(dateBegin) && candleDate.isBefore(dateEnd);
        }).toList();

        List<CandleDto> upCandles = new ArrayList<>();
        List<CandleDto> downCandles = new ArrayList<>();

        for (int i = 0; i < candles.size() - 1; i++) {
            if (Float.parseFloat(candles.get(i + 1).getClose()) > Float.parseFloat(candles.get(i).getClose())) {
                upCandles.add(candles.get(i + 1));
            } else {
                downCandles.add(candles.get(i + 1));
            }
        }

        float rs = ema(upCandles, upCandles.size()) / ema(downCandles, downCandles.size());

        return 100 - 100 / (1 + rs);
    }

    private Float ema(List<CandleDto> candles, int period) {
        float multiplier = (float) 2 / (period + 1);
        float[] ema = new float[candles.size()];
        ema[0] = Float.parseFloat(candles.get(0).getClose());
        for (int i = 0; i < ema.length - 1; i++) {
            ema[i + 1] = ema[i] * (1 - multiplier) + multiplier * Float.parseFloat(candles.get(i + 1).getClose());
        }

        return ema[ema.length - 1];
    }

}
