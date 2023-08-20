package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.SomeUserDetails;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SomeUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new SomeUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}