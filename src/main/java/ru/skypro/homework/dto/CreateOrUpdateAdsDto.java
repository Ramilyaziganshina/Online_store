package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateOrUpdateAdsDto {
    @NotBlank(message = "title need filled")
    private String title;
    @NotNull(message = "price need not null")
    @Positive(message = "price need positive")
    private Integer price;
    @NotBlank(message = "description need filled")
    private String description;
}
