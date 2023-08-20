package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Image;

import java.io.IOException;
import java.util.Optional;

public interface ImageService{
    Image createdImage(Ad ad, MultipartFile file);
    public Image updateImage(Ad ad, MultipartFile file);
    Optional<Image> findImage(Long id);
}