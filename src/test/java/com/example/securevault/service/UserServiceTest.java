package com.example.securevault.service;

import com.example.securevault.entity.User;
import com.example.securevault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2User oauth2User;

    @InjectMocks
    private UserService userService;

    @Test
    void findOrCreateUser_ShouldReturnExistingUser() {
        String sub = "user123";
        String email = "test@example.com";
        
        User existingUser = new User();
        existingUser.setSub(sub);
        existingUser.setEmail(email);
        
        when(oauth2User.getName()).thenReturn(sub);
        when(userRepository.findBySub(sub)).thenReturn(Optional.of(existingUser));

        User result = userService.findOrCreateUser(oauth2User);

        assertEquals(existingUser, result);
        verify(userRepository).findBySub(sub);
        verify(userRepository, never()).save(any());
    }

    @Test
    void findOrCreateUser_ShouldCreateNewUserWithEmail() {
        String sub = "user123";
        String email = "test@example.com";
        
        User newUser = new User();
        newUser.setSub(sub);
        newUser.setEmail(email);
        
        when(oauth2User.getName()).thenReturn(sub);
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(userRepository.findBySub(sub)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.findOrCreateUser(oauth2User);

        assertEquals(sub, result.getSub());
        assertEquals(email, result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findOrCreateUser_ShouldCreateNewUserWithUsername() {
        String sub = "user123";
        String username = "testuser";
        
        User newUser = new User();
        newUser.setSub(sub);
        newUser.setEmail(username);
        
        when(oauth2User.getName()).thenReturn(sub);
        when(oauth2User.getAttribute("email")).thenReturn(null);
        when(oauth2User.getAttribute("username")).thenReturn(username);
        when(userRepository.findBySub(sub)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.findOrCreateUser(oauth2User);

        assertEquals(sub, result.getSub());
        assertEquals(username, result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findOrCreateUser_ShouldThrowExceptionWhenNoEmailOrUsername() {
        String sub = "user123";
        
        when(oauth2User.getName()).thenReturn(sub);
        when(oauth2User.getAttribute("email")).thenReturn(null);
        when(oauth2User.getAttribute("username")).thenReturn(null);
        when(userRepository.findBySub(sub)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> 
            userService.findOrCreateUser(oauth2User)
        );
        
        verify(userRepository, never()).save(any());
    }
}