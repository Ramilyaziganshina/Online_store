package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UserUpdateDto;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NoContentException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handlerException(UnauthorizedException e) {
        return String.format("%s %s", HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public String handlerException(ForbiddenException e) {
        return String.format("%s %s", HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handlerException(NotFoundException e) {
        return String.format("%s %s", HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoContentException.class)
    public String handlerException(NoContentException e) {
        return String.format("%s %s", HttpStatus.NO_CONTENT.value(), e.getMessage());
    }

    private final UserService userService;
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(
            summary = "Установка нового пароля", tags = "Пользователь",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "*/*",
                                    schema = @Schema(implementation = Collection.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)

            }
    )

    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@RequestBody @Valid NewPasswordDto newPasswordDto) {
        String login =SecurityContextHolder.getContext().getAuthentication().getName();
        userService.setPassword(login, newPasswordDto);
        return ResponseEntity.ok("Пароль успешно изменен!");
    }

    @Operation(
            summary = "Информации о текущем пользователе", tags = "Пользователь",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "*/*",
                                    schema = @Schema(implementation = Collection.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMeUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.getMeUser(login);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Обновление информации о текущем пользователе", tags = "Пользователь",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "*/*",
                                    schema = @Schema(implementation = Collection.class))}),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )

    @PatchMapping("/me")
    public ResponseEntity<UserUpdateDto> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.updateUser(login, userUpdateDto));
    }

    @Operation(
            summary = "Обновление аватарки пользователя", tags = "Пользователь",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "multipart/form-data",
                                    schema = @Schema(implementation = Object.class))}),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    @PatchMapping("/me/image")
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateUserAvatar(login,image);

        return ResponseEntity.ok().build();
    }
}