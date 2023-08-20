package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NoContentException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.AdsService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

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

    private final AdsService adsService;
    private static final Logger logger = LoggerFactory.getLogger(AdsController.class);

    @Operation(
            summary = "Получить все объявления", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDto.class))})
            }
    )
    @GetMapping
    public ResponseEntity<AllAdsOfUserDto> getAllAds() {

        logger.info("getAllAds is active");
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @Operation(
            summary = "Получить все объявления пользователя", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "*/*",
                                    schema = @Schema(implementation = Collection.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<AllAdsOfUserDto> getALLAdsMe() {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        AllAdsOfUserDto allAdsOfUserDto = adsService.getAllAdsMe(authentication);

        return ResponseEntity.ok(allAdsOfUserDto);
    }

    @Operation(
            summary = "Добавить новое объявление", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDto.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised")

            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(
            @RequestPart("image") MultipartFile image,
            Authentication authentication,
            @RequestPart("properties") @Valid CreateOrUpdateAdsDto createOrUpdateAdsDto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.addAds(authentication.getName(), image, createOrUpdateAdsDto));
    }

    @Operation(
            summary = "Получение информации об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "*/*",
                                    schema = @Schema(implementation = Collection.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FullInfoAdsDto> getAdsById(
            @PathVariable("id") Long id) {
        logger.info("getAdsById is active {}", id);
        return ResponseEntity.ok(adsService.getAdsById(id));
    }

    @Operation(
            summary = "Удаление объявления", tags = "Объявления",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Long id) {
        adsService.removeAdById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Обновление информации об объявлении", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "*/*",
                                    schema = @Schema(implementation = Collection.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping(value = "/{id}")
    public ResponseEntity<AdsDto> editAd(@PathVariable("id") Integer id,
                                         @RequestBody @Valid CreateOrUpdateAdsDto updateAdsDto) {
        logger.info("is now active editAd + {}", updateAdsDto);
        return ResponseEntity.ok(adsService.updateAd(Long.valueOf(id), updateAdsDto));
    }

    @Operation(
            summary = "Изменить изображение", tags = "Объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdsDto.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised")

            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@RequestParam MultipartFile image, @PathVariable Long id) {
        adsService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получить комментарии объявления", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AllCommentOfAdsDto.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<AllCommentOfAdsDto> getCommentOfAd(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.getComments(id));
    }

    @Operation(
            summary = "Добавление нового комментария к объявлению", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentAdsDto.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<CommentAdsDto> addCommentOfAd(@PathVariable Integer id,
                                                        @RequestBody CreateOrUpdateComment text) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        CommentAdsDto commentAdsDto = adsService.addComment(id, text, login);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentAdsDto);
    }

    @Operation(
            summary = "Удалить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @DeleteMapping(value = "/{adId}/comments/{commentID}")
    public ResponseEntity delCommentOfAd(@PathVariable("adId") Integer adId, @PathVariable("commentID") Integer commentID) {
        logger.info("delete comment {} {}", adId, commentID);
        adsService.deleteComment(adId, commentID);
        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "Обновить комментарий", tags = "Комментарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentAdsDto.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PatchMapping(value = "/{adId}/comments/{commentID}")
    public ResponseEntity<CommentAdsDto> updateCommentOfAd(@PathVariable("adId") Integer adId,
                                                           @PathVariable("commentID") Integer commentID,
                                                           @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        logger.info("delete comment {} {}", adId, commentID);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(adsService.patchComment(adId, commentID, createOrUpdateComment, login));
    }
}
