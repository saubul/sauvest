package ru.sauvest.social.mapper;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sauvest.baseservices.mapper.EntityMapper;
import ru.sauvest.social.dto.VoteDto;
import ru.sauvest.social.entity.Vote;
import ru.sauvest.social.service.PostService;
import ru.sauvest.social.service.UserService;

@Mapper(componentModel = "spring")
public abstract class VoteMapper extends EntityMapper<Vote, VoteDto> {

    @Autowired
    protected PostService postService;

    @Autowired
    protected PostMapper postMapper;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapper userMapper;

    @AfterMapping
    protected void afterEntityMapping(Vote vote, @MappingTarget VoteDto voteDto) {
        voteDto.setUsername(vote.getUser().getUsername());
        voteDto.setPostId(vote.getPost().getId());
    }

    @AfterMapping
    protected void afterDtoMapping(@MappingTarget Vote vote, VoteDto voteDto) {
        vote.setPost(postMapper.dtoToEntity(postService.findById(voteDto.getPostId())));
        vote.setUser(userMapper.dtoToEntity(userService.findByUsername(voteDto.getUsername())));
    }

}
