package ru.sauvest.baseservices.mapper;

public abstract class EntityMapper<ENTYTYCLASS, DTOCLASS> {

    public abstract DTOCLASS entityToDto(ENTYTYCLASS entity);

    public abstract ENTYTYCLASS dtoToEntity(DTOCLASS dto);
}
