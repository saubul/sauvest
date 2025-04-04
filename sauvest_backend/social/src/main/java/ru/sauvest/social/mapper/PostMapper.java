package ru.sauvest.social.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.PostDto;
import ru.sauvest.social.entity.Post;
import ru.sauvest.social.service.InstrumentService;
import ru.sauvest.social.service.UserService;

import java.util.Collections;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper extends EntityMapper<Post, PostDto> {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected InstrumentService instrumentService;

    @Autowired
    protected InstrumentMapper instrumentMapper;

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget Post post, PostDto postDTO) {
        post.setUser(userMapper.dtoToEntity(userService.findByUsername(postDTO.getUsername())));
        post.setInstruments(
                Optional.ofNullable(postDTO.getInstruments())
                        .map(instrumentDTOS ->
                                instrumentDTOS.stream()
                                        .map(instrumentDto -> instrumentMapper.dtoToEntity(instrumentService.findByFigi(instrumentDto.getFigi())))
                                        .toList())
                        .orElse(Collections.emptyList())
        );
    }

    @AfterMapping
    protected void afterEntityMapping(Post post, @MappingTarget PostDto postDto) {
        postDto.setUsername(post.getUser().getUsername());
        postDto.setVoteCount(post.getVoteCount());
        postDto.setName(post.getUser().getName());
        postDto.setSurname(post.getUser().getSurname());
        postDto.setInstruments(
                post.getInstruments()
                        .stream()
                        .map(instrumentMapper::entityToDto)
                        .toList()
        );
    }

}
