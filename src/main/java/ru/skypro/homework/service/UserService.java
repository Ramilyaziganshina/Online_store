package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.User;

import java.util.Optional;

public interface UserService {
    UserDto getMeUser(String login);

    void setPassword(String login, NewPasswordDto newPasswordDto);

    UserUpdateDto updateUser(String login, UserUpdateDto userUpdateDto);

    void registerUser(Register register, String encodedPassword, Role role);

    Optional<User> updatePassword(String login, String newPassword);

    void updateUserAvatar(String login, MultipartFile image);

    Avatar findAvatar(String name);
}