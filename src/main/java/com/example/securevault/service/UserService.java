package com.example.securevault.service;

import com.example.securevault.entity.User;
import com.example.securevault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(OAuth2User oauth2User) {
        String sub = oauth2User.getName();
        Optional<User> existingUser = userRepository.findBySub(sub);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User();
            newUser.setSub(sub);

            // Safely retrieve email attribute
            String email = oauth2User.getAttribute("email");
            if (email == null) {
                email = oauth2User.getAttribute("username");
            }
            if (email == null) {
                throw new IllegalStateException("Could not determine email from user attributes");
            }

            newUser.setEmail(email);
            return userRepository.save(newUser);
        }
    }
}

