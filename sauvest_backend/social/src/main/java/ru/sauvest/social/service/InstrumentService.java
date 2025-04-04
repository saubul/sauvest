package ru.sauvest.social.service;

import ru.sauvest.social.dto.InstrumentDto;
import ru.sauvest.social.entity.Instrument;

import java.util.List;

public interface InstrumentService {

	Instrument save(InstrumentDto instrumentDto);

	void saveAll(List<InstrumentDto> instrumentDtoList);

	List<InstrumentDto> findByPost(Long postId);

	InstrumentDto findByFigi(String figi);
	
}
