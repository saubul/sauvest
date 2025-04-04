package ru.sauvest.social.service;

import ru.sauvest.social.dto.CandleDto;
import ru.sauvest.social.dto.InstrumentAnalysisHistoryDto;

import java.util.Date;
import java.util.List;

public interface InstrumentAnalysisHistoryService {

	List<InstrumentAnalysisHistoryDto> findByUsernameAndFigi(String username, String figi);

	InstrumentAnalysisHistoryDto save(InstrumentAnalysisHistoryDto instrumentAnalysisHistoryDTO);

	InstrumentAnalysisHistoryDto executeTechnicalAnalysis(List<CandleDto> candleDtoList, Date date,
														  String username, String figi);

}
