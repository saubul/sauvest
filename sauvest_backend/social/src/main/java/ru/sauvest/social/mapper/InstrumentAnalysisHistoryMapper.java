package ru.sauvest.social.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.InstrumentAnalysisHistoryDto;
import ru.sauvest.social.entity.InstrumentAnalysisHistory;
import ru.sauvest.social.service.InstrumentService;
import ru.sauvest.social.service.UserService;

@Mapper(componentModel = "spring")
public abstract class InstrumentAnalysisHistoryMapper extends EntityMapper<InstrumentAnalysisHistory, InstrumentAnalysisHistoryDto> {

    @Autowired
    protected InstrumentService instrumentService;

    @Autowired
    protected InstrumentMapper instrumentMapper;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @AfterMapping
    protected void afterEntityMapping(InstrumentAnalysisHistory entity, @MappingTarget InstrumentAnalysisHistoryDto dto) {
        dto.setUsername(entity.getUser().getUsername());
        dto.setFigi(entity.getInstrument().getFigi());
    }

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget InstrumentAnalysisHistory entity, InstrumentAnalysisHistoryDto dto) {
        entity.setInstrument(instrumentMapper.dtoToEntity(instrumentService.findByFigi(dto.getFigi())));
        entity.setUser(userMapper.dtoToEntity(userService.findByUsername(dto.getUsername())));
    }

}
