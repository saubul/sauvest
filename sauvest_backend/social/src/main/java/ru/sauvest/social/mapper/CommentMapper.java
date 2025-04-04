package ru.sauvest.social.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.CommentDto;
import ru.sauvest.social.entity.Comment;
import ru.sauvest.social.service.PostService;
import ru.sauvest.social.service.UserService;

@Mapper(componentModel = "spring")
public abstract class CommentMapper extends EntityMapper<Comment, CommentDto> {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected PostService postService;

    @Autowired
    protected PostMapper postMapper;

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget Comment comment, CommentDto commentDto) {
        comment.setUser(userMapper.dtoToEntity(userService.findByUsername(commentDto.getUsername())));
        comment.setPost(postMapper.dtoToEntity(postService.findById(commentDto.getPostId())));
    }

}
