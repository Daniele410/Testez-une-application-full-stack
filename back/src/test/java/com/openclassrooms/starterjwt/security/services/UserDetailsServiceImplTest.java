package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;
    @Mock
    UserRepository userRepository;

    @Test
    void testLoadUserByUsername_shouldReturnUserDetailsImpl() {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("mario@example.com");
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setPassword("password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // then
        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

    }

    @Test
    void testLoadUserByUsername_shouldReturnException() {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("mario@example.com");
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.empty());

        // when // then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(user.getEmail()));

    }

    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        // given
        String username = "testuser";
        User user = new User();
        user.setId(1L);
        user.setEmail("mario@example.com");
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setPassword("password");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // then
        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // given
        String username = "nonexistentuser";

        // Configura il comportamento del repository mock quando viene chiamato findByEmail
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // when/then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }

}