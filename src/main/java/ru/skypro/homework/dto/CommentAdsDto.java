package ru.skypro.homework.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentAdsDto {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Integer pk;
    private LocalDateTime createdAt;
    private String text;
}
