package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllCommentOfAdsDto {
    private Integer count;
    private List<CommentAdsDto> results;
}
