package ru.sauvest.social.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sauvest.social.dto.InstrumentDto;
import ru.sauvest.social.entity.Instrument;
import ru.sauvest.social.mapper.InstrumentMapper;
import ru.sauvest.social.repository.InstrumentRepository;
import ru.sauvest.social.service.InstrumentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;

    private final InstrumentMapper instrumentMapper;

    @Override
    public List<InstrumentDto> findByPost(Long postId) {
        return instrumentRepository.findByPost(postId).stream()
                .map(instrumentMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public Instrument save(InstrumentDto instrumentDto) {
        return instrumentRepository.save(instrumentMapper.dtoToEntity(instrumentDto));
    }

    @Override
    @Transactional
    public void saveAll(List<InstrumentDto> instrumentDtoList) {
        instrumentRepository.saveAll(
                instrumentDtoList.stream()
                        .map(instrumentMapper::dtoToEntity)
                        .toList()
        );
    }

    @Override
    public InstrumentDto findByFigi(String figi) {
        return instrumentRepository.findByFigi(figi)
                .map(instrumentMapper::entityToDto)
                .orElseThrow(() -> new RuntimeException(String.format("Instrument with figi \"%s\" not found", figi)));
    }

}
