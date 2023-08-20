package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.SomeUserDetailsService;
import ru.skypro.homework.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SomeUserDetailsService manager;

    private final PasswordEncoder encoder;

    private final UserService userService;

    @Override
    public boolean login(String userName, String password) {
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register, Role role) {
        if (register.getUsername() == null || register.getUsername().isBlank()
                || register.getFirstName() == null || register.getFirstName().isBlank()
                || register.getLastName() == null || register.getLastName().isBlank()
                || register.getPhone() == null || register.getPhone().isBlank()
                || register.getPassword() == null || register.getPassword().isBlank())
            throw new BadRequestException("Incorrect argument");

        userService.registerUser(register, this.encoder.encode(register.getPassword()), role);
        return true;
    }
}