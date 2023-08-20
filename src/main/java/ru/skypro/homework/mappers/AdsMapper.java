package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.AllAdsOfUserDto;
import ru.skypro.homework.dto.CreateOrUpdateAdsDto;
import ru.skypro.homework.dto.FullInfoAdsDto;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.Image;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    /**
     * adsToAdsDto(Ad ad):
     * Этот метод принимает объект типа Ad и отображает его на объект типа AdsDto.
     * Он выполняет отдельные отображения для различных полей объекта Ad.
     */

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "ad", target = "image",  qualifiedByName = "mapImageToString")
    AdsDto fromModel(Ad ad);

    /**
     * adsToFullAdsDto(Ad ads):
     * Этот метод выполняет более подробное отображение объекта Ad на объект типа FullInfoAdsDto.
     * В отличие от предыдущего метода, он также выполняет отображение полей связанного объекта author.
     */
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "image.ad", target = "image", qualifiedByName = "mapImageToString")
    @Mapping(source = "author.phone", target = "phone")
    FullInfoAdsDto toFullInfoAdsDto(Ad ads);

    /**
     * adsDtoToAds(AdsDto adDto):
     * Этот метод выполняет обратное отображение объекта типа AdsDto на объект типа Ad. Он игнорирует поле images.
     */

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author.id", source = "author")
    @Mapping(target = "image", ignore = true)
    Ad toModel(AdsDto adsDto);

    Ad createOrUpdateToAd(CreateOrUpdateAdsDto createOrUpdateAdsDto);
    CreateOrUpdateAdsDto AdToCreateOrUpdate(Ad ad);
    /**
     * adsListToResponseWrapperAdsDto(Integer sizeList, List<Ad> entityList):
     * Этот метод отображает список объектов типа Ad на объект типа AllAdsOfUserDto,
     * который представляет обертку для списка объявлений пользователя.
     * Он также выполняет отображение поля sizeList на поле count и полей entityList на поле results.
     */
    @Mapping(target = "count", source = "sizeList")
    @Mapping(target = "results", source = "list")
    AllAdsOfUserDto adsListToResponseWrapperAdsDto(Integer sizeList, List<Ad> list);

    // метод mapImageToString преобразования Image в String
    @Named("mapImageToString")
    default String getImageString(Ad ad) {
        Image image = ad.getImage();
        if(image == null){
            return null;
        }
        return "/picture/"+image.getAd().getId().toString();
    }
}