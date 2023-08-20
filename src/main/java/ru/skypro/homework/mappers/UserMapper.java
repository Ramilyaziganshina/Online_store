package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UserUpdateDto;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User UserUpdateDtoToUser(User user, UserUpdateDto userUpdateDto) {
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setPhone(userUpdateDto.getPhone());
        return user;
    }

    UserUpdateDto UserToUserUpdateDto(User user);

    @Mapping(source = "username", target = "email")
    @Mapping(source = "user", target = "image", qualifiedByName = "mapImageToString")
    UserDto UsertoUserDto(User user);

    @Named("mapImageToString")
    default String getImageString(User user) {
        Avatar avatar = user.getAvatar();
        if (avatar == null) {
            return null;
        }
        return "/avatar/" + avatar.getId().toString();
    }
}