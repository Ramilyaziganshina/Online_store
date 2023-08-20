package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Getter
@Setter
public class NewAdsDto {
    @NotNull(message = "id need not be null")
    private Integer id;
    @NotNull(message = "image need not be null")
    private String image;
    @NotNull
    private Integer pk;
    @Positive
    private Integer price;
    @NotNull
    private String title;
}
