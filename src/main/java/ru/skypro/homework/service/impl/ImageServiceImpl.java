package ru.skypro.homework.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class ImageServiceImpl implements ImageService{
    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image createdImage(Ad ad, MultipartFile file){
        byte[] image = null;
        try {
            image = file.getBytes();
        }catch (IOException e){
            logger.error("Не удалось извлечь содержимое изображения");
        }

        Image adImage = new Image();
        adImage.setImage(image);
        adImage.setAd(ad);
        return imageRepository.save(adImage);
    }

    @Override
    public Image updateImage(Ad ad, MultipartFile file) {
        byte[] image = null;
        try {
            image = file.getBytes();
        }catch (IOException e){
            logger.error("Не удалось извлечь содержимое изображения");
        }

        Optional<Image> adImage = imageRepository.getImageByAd_Id(ad.getId());
        if(adImage.isEmpty()){
            throw new NotFoundException("Not found image");
        }
        Image changeImage = adImage.get();
        changeImage.setImage(image);
        changeImage.setAd(ad);
        changeImage.setId(ad.getImage().getId());
        return imageRepository.save(changeImage);
    }

    @Override
    public Optional<Image> findImage(Long id) {
        return imageRepository.findById(id);
    }
}