package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    UserRepository userRepository;

    @Test
    void testUserDelete_shouldCallUserRepositoryDeleteById() {
        // given
        long userId = 1L;

        // when
        userService.delete(userId);

        // then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testFindById_UserExists() {
        // given
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        User resultUser = userService.findById(userId);

        // then
        assertNotNull(resultUser);
        assertEquals(userId, resultUser.getId());
        assertEquals("test@example.com", resultUser.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_UserNotExists() {
        // given
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        User resultUser = userService.findById(userId);

        // then
        assertNull(resultUser);
        verify(userRepository, times(1)).findById(userId);
    }

}