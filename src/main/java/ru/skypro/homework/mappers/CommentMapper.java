package ru.skypro.homework.mappers;

import org.mapstruct.*;
import ru.skypro.homework.dto.AllCommentOfAdsDto;
import ru.skypro.homework.dto.CommentAdsDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.models.*;

import java.util.List;

@Mapper(componentModel = "spring")

public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorImage", source = "author", qualifiedByName = "mapImageToString")
    CommentAdsDto commentToCommentAdsDto(Comment comment);

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author.id", source = "author")
    Comment commentAdsDtoToComment(CommentAdsDto commentAdsDto);

    List<CommentAdsDto> commentsToAllCommentsDto(List<Comment> commentAdsDtos);

    @Mapping(target = "count", source = "sizeList")
    @Mapping(target = "results", source = "entityList")
    AllCommentOfAdsDto allCommentsDtoToComments(Integer sizeList, List<CommentAdsDto> entityList);

    @Named("mapImageToString")
    default String getImageString(User user) {
        Avatar avatar = user.getAvatar();
        if(avatar == null){
            return null;
        }
        return "/avatar/" + avatar.getId().toString();
    }
}