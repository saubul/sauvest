package ru.sauvest.social.mapper;

import org.mapstruct.Mapper;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.InstrumentDto;
import ru.sauvest.social.entity.Instrument;

@Mapper(componentModel = "spring")
public abstract class InstrumentMapper extends EntityMapper<Instrument, InstrumentDto> {
}
