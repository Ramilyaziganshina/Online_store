package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UserUpdateDto;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.Avatar;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {


    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private  final UserMapper userMapper;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AvatarRepository avatarRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

        this.avatarRepository = avatarRepository;
        this.encoder = encoder;
    }

    @Override
    public void setPassword(String login, NewPasswordDto newPasswordDto) {
        User user = userRepository.findUserByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        if (!encoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Bad Credentials");
        }
        user.setPassword(encoder.encode(newPasswordDto.getNewPassword()));

        userRepository.save(user);

    }

    @Override
    public UserDto getMeUser(String login) {
        User user = userRepository.findUserByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return userMapper.UsertoUserDto(user);
    }

    @Override
    public UserUpdateDto updateUser(String login, UserUpdateDto userUpdateDto) {
        if(userUpdateDto.getFirstName() == null || userUpdateDto.getFirstName().isBlank()
                || userUpdateDto.getLastName() == null || userUpdateDto.getLastName().isBlank()
                || userUpdateDto.getPhone() == null || userUpdateDto.getPhone().isBlank()) throw new BadRequestException("Incorrect argument");
        Optional<User> optionalUser = userRepository.findUserByUsername(login);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        User thisUser = optionalUser.get();
        thisUser = userMapper.UserUpdateDtoToUser(thisUser, userUpdateDto);
        thisUser = userRepository.save(thisUser);

        return userMapper.UserToUserUpdateDto(thisUser);
    }

//    public List<UserDetails> getUserDetails() {
//        List<UserDetails> result = new ArrayList<>();
//        userRepository.findAll()
//                .forEach(u -> result.add(org.springframework.security.core.userdetails.User.builder()
//                                .username(u.getUsername())
//                                .password(u.getPassword())
//                                .roles(Role.USER.name())
//                                .build()
//                        )
//                );
//        return result;
//    }

    public void registerUser(Register register, String encodedPassword, Role role) {
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(encodedPassword);
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(role);
        userRepository.save(user);
    }

    public Optional<User> updatePassword(String login, String newPassword) {
        Optional<User> userOptional = userRepository.findUserByUsername(login);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        user.setPassword(newPassword);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public void updateUserAvatar(String login, MultipartFile image) {
        User user = userRepository.findUserByUsername(login)
                .orElseThrow(() -> new IllegalArgumentException("Подьзователь не найден"));

        updateAvatar(user, image);
    }

    @Override
    public Avatar findAvatar(String name) {
        Optional<User> user = userRepository.findUserByUsername(name);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        Optional<Avatar> avatar = avatarRepository.findByUserId(user.get().getId());
        if(avatar.isEmpty()){
            throw new NotFoundException("Avatar not found");
        }
        return avatar.get();

    }

    public Avatar updateAvatar(User user, MultipartFile file) {
        byte[] image = null;
        try {
            image = file.getBytes();
        } catch (IOException e) {
            logger.error("Не удалось извлечь содержимое изображения");
        }
        Optional<Avatar> avatarOptional = avatarRepository.findByUserId(user.getId());
        Avatar userAvatar;
        if (avatarOptional.isEmpty()) {
            userAvatar = new Avatar();
        } else {
            userAvatar = avatarOptional.get();
        }
        userAvatar.setImage(image);
        userAvatar.setUser(user);

        user.setAvatar(avatarRepository.save(userAvatar));


        return userRepository.save(user).getAvatar();
    }
}