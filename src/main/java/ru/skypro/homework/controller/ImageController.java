package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.impl.AvatarServiceImpl;

import java.util.Optional;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {
    private final ImageService imageService;
    private final AvatarServiceImpl avatarService;

    public ImageController(ImageService imageService, AvatarServiceImpl avatarService) {
        this.imageService = imageService;
        this.avatarService = avatarService;
    }

    @GetMapping(value = "/picture/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable("id") Long id) {
        Optional<Image> image = imageService.findImage(id);
        if (image.isEmpty()) {
            throw new NotFoundException("Не найдена картинка");
        }
        return ResponseEntity.ok(image.get().getImage());
    }

    @GetMapping(value = "/avatar/{id}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Long id) {
        Optional<Avatar> avatar = avatarService.findAvatar(id);
        if (avatar.isEmpty()) {
            throw new NotFoundException("Не найдена картинка");
        }
        return ResponseEntity.ok(avatar.get().getImage());
    }
}