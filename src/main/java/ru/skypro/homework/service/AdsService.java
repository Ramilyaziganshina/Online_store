package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.models.Ad;

import java.io.IOException;

public interface AdsService {
    AllAdsOfUserDto getAllAds();

    AllAdsOfUserDto getAllAdsMe(String user);

    AdsDto addAds(String username, MultipartFile image, CreateOrUpdateAdsDto createOrUpdateAdsDto) throws IOException;

    FullInfoAdsDto getAdsById(Long id);

    void removeAdById(Long id);

    AdsDto updateAd(Long id, CreateOrUpdateAdsDto adDto);

    void updateAdImage(Long id, MultipartFile image);

    AdsDto editAd(Long id, MultipartFile image, Ad newAd);

    AllCommentOfAdsDto getComments(Long id);

    CommentAdsDto addComment(Integer id, CreateOrUpdateComment text, String login);

    void deleteComment(Integer adPk, Integer id);

    CommentAdsDto patchComment(Integer adPk, Integer id, CreateOrUpdateComment createOrUpdateComment, String login);

    public boolean checkAccessForAd(String username, long id);

    boolean checkAccessForAdComment(String username, long id);
}