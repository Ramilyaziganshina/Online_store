package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class NewInfoUserDto {
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "")
    private String phone;
}