package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    //Эта аннотация гарантирует, что значения не может быть null
    @NotNull(message = "id need be not null")
    private Integer id;

    //Эта аннотация гарантирует, что значения полей не могут быть пустыми или содержать только пробелы
    @NotBlank(message = "firstName need be filled")
    private String firstName;

    @NotBlank(message = "lastName need be filled")
    private String lastName;

    @NotBlank(message = "email must be filled")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "email need be correct")
    private String email;

    @NotBlank(message = "phone must be filled")
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "number need start from +7 and after 10 numbers")
    private String phone;

    private String role;

    private String image;
}