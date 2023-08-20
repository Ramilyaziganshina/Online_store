package ru.skypro.homework.dto;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;

@Data
public class FullInfoAdsDto {
    private Integer pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer price;
    private String title;

}
