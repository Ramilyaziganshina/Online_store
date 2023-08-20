package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Comment;
import ru.skypro.homework.models.Image;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class AdsServiceImpl implements AdsService {
    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);

    private final AdRepository adRepository;
    private final ImageServiceImpl imageService;
    private final AdsMapper adsMapper;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;

    public AdsServiceImpl(AdRepository adRepository, ImageServiceImpl imageService, AdsMapper adsMapper,
                          UserRepository userRepository, CommentMapper commentMapper, CommentRepository commentRepository, ImageRepository imageRepository) {
        this.adRepository = adRepository;
        this.imageService = imageService;
        this.adsMapper = adsMapper;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.imageRepository = imageRepository;

    }

    public AllAdsOfUserDto getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return adsMapper.adsListToResponseWrapperAdsDto(ads.size(), ads);
    }

    @Override
    public AllAdsOfUserDto getAllAdsMe(String username) {
        User user = getUserByUsername(username);
        List<Ad> ads = adRepository.getAllByAuthor(user);
        return adsMapper.adsListToResponseWrapperAdsDto(ads.size(), ads);
    }

    private User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            logger.error("user is null {}", userOptional);
            throw new NotFoundException("Don't found data");
        }
        return userOptional.get();
    }

    @Override
    public AdsDto addAds(String username, MultipartFile image, CreateOrUpdateAdsDto createOrUpdateAdsDto) throws IOException {
        User user = getUserByUsername(username);
        Ad ad = adsMapper.createOrUpdateToAd(createOrUpdateAdsDto);
        ad.setAuthor(user);
        ad.setId(null);

        String imageID = imageService.createdImage(ad, image).getId().toString();
        AdsDto adsDto = adsMapper.fromModel(ad);
        adsDto.setImage("/picture/" + imageID);
        return adsMapper.fromModel(adRepository.save(ad));
    }

    public FullInfoAdsDto getAdsById(Long id) {
        FullInfoAdsDto fullInfoAdsDto = adsMapper.toFullInfoAdsDto(findAdsById(id));

        logger.info("getAdsById {}", fullInfoAdsDto);
        Optional<Image> image = imageRepository.getImageByAd_Id(fullInfoAdsDto.getPk());
        if(image.isEmpty()){
            throw new NotFoundException("Not found image");
        }
        logger.info("getAdsById by Service is active {}", fullInfoAdsDto);
        return fullInfoAdsDto;
    }

    public Ad findAdsById(Long id) {
        Optional<Ad> adOptional = adRepository.findById(id);

        if (adOptional.isPresent()) {
            return adOptional.get();
        } else {
            throw new NotFoundException("Не найден");
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.checkAccessForAd(principal.username, #id)")
    public void removeAdById(Long id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        logger.info("id of ad {}", adOptional.get());
        adRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.checkAccessForAd(principal.username, #id)")
    public AdsDto updateAd(Long id, CreateOrUpdateAdsDto adDto) {
        if (adDto.getTitle() == null || adDto.getTitle().isBlank()
                || adDto.getDescription() == null || adDto.getDescription().isBlank()
                || adDto.getPrice() == null) throw new BadRequestException("Incorrect argument");
        Optional<Ad> adOptional = adRepository.findById(id);
        Optional<User> user = userRepository.findById(adOptional.get().getAuthor().getId());
        if (user.isEmpty()) {
            throw new NotFoundException("data not found");
        }
        Ad ad = adOptional.get();
        ad.setDescription(adDto.getDescription());
        ad.setPrice(BigDecimal.valueOf(adDto.getPrice()));
        ad.setTitle(adDto.getTitle());
        adRepository.save(ad);
        return adsMapper.fromModel(ad);
    }


    public void updateAdImage(Long id, MultipartFile image) {
        Optional<Ad> adOptional = adRepository.findById(id);

        if (adOptional.isEmpty()) {
            throw new NotFoundException("not found");
        }
        Ad ad = adOptional.get();
        imageService.updateImage(ad, image);

    }


    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.checkAccessForAd(principal.username, #id)")
    @Override
    public AdsDto editAd(Long id, MultipartFile image, Ad newAd) {
        return new AdsDto();
    }

    @Override
    public AllCommentOfAdsDto getComments(Long id) {
        List<CommentAdsDto> list = commentMapper.commentsToAllCommentsDto(commentRepository.getAllByAd_Id(id));

        return commentMapper.allCommentsDtoToComments(list.size(), list);
    }

    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.checkAccessForAdComment(principal.username, #CommentId)")
    @Override
    public void deleteComment(Integer adId, Integer CommentId) {
        Optional<Ad> adOptional = adRepository.findById(Long.valueOf(adId));
        Optional<Comment> commentOptional = commentRepository.findCommentByAdAndId(adOptional.get(), Long.valueOf(CommentId));
        logger.info("deleteComment is {}", commentOptional);
        if (commentOptional.isEmpty()) {
            throw new NotFoundException("data not found");
        }

        commentRepository.delete(commentOptional.get());

    }

    @Override
    public CommentAdsDto addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, String login) {
        if(createOrUpdateComment.getText() == null || createOrUpdateComment.getText().isBlank()) throw new BadRequestException("Incorrect argument");
        Optional<User> user = userRepository.findUserByUsername(login);
        Optional<Ad> adOptional = adRepository.findById(Long.valueOf(id));
        if (user.isEmpty() || adOptional.isEmpty()) {
            throw new NotFoundException("not found");
        }
        String text = createOrUpdateComment.getText();
        Comment comment = new Comment();
        comment.setText(text);
        logger.info("Text of addComment {}", text);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(user.get());
        comment.setAd(adOptional.get());
        comment.setId(null);
        return commentMapper.commentToCommentAdsDto(commentRepository.save(comment));
    }

    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.checkAccessForAdComment(principal.username, #commentID)")
    @Override
    public CommentAdsDto patchComment(Integer adPk, Integer commentID, CreateOrUpdateComment createOrUpdateComment, String login) {
        if(createOrUpdateComment.getText() == null || createOrUpdateComment.getText().isBlank()) throw new IllegalArgumentException();

        Comment adsComment = getAdsComment(Long.valueOf(commentID), Long.valueOf(adPk));
        adsComment.setText(createOrUpdateComment.getText());
        commentRepository.save(adsComment);
        return commentMapper.commentToCommentAdsDto(commentRepository.save(adsComment));
    }
    public Comment getAdsComment(Long commentId, Long adId) {
        logger.info("Getting comment with id: {} for ad with id: {}", commentId, adId);
        return commentRepository.findByIdAndAdId(commentId, adId).orElseThrow();
    }

    @Override
    public boolean checkAccessForAd(String username, long id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if(adOptional.isEmpty()){
            throw new NotFoundException("Ad Not Found");
        }
        if(userRepository.findUserByUsername(username).get().getRole().equals(Role.ADMIN)){
            return true;
        }
        return adRepository.findById(id).get().getAuthor().getUsername().equals(username);
    }

    @Override
    public boolean checkAccessForAdComment(String username, long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if(commentOptional.isEmpty()){
            throw new NotFoundException("Comment Not Found");
        }
        if(userRepository.findUserByUsername(username).get().getRole().equals(Role.ADMIN)){
            return true;
        }

        return commentRepository.findCommentById(id).get().getAuthor().getUsername().equals(username);
    }

}