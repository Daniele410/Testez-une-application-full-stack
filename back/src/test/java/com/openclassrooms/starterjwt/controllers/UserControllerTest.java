package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;
    @Mock
    Authentication authentication;
    @Mock
    SecurityContext securityContext;
    @Mock
    UserDetails userDetails;
    @InjectMocks
    private UserController userController;


    @Test
    public void testUserFindById_shouldReturnResponseOk() {
        // given
        long userId = 1L;
        User mockUser = User.builder()
                .id(1L)
                .firstName("Mario")
                .lastName("Rossi")
                .password("password")
                .email("mario@mail.com")
                .build();

        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(1L);
        mockUserDto.setFirstName("Mario");

        when(userService.findById(1L)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        // when
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(userId));

        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockUser.getFirstName(), mockUserDto.getFirstName());
    }

    @Test
    public void testUserFindById_shouldReturnResponseNotFoundCode() {
        // given
        long userId = 2L;
        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(1L);
        mockUserDto.setFirstName("Mario");

        when(userService.findById(2L)).thenReturn(null);

        // when
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(userId));

        // then
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void testUserFindById_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = userController.findById("nonNumericId");
        // then
        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    void testUserSave_ValidId_UserDeletedSuccessfully() {
        // given
        long userId = 1L;
        User user = User.builder()
                .id(1L)
                .firstName("Mario")
                .lastName("Rossi")
                .password("password")
                .email("mario@mail.com")
                .build();

        when(userDetails.getUsername()).thenReturn("mario@mail.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);

        // when
        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        // then
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    void saveUserNull_shouldReturnResponseNotFound() {
        // given
        long userId = 1L;

        when(userService.findById(userId)).thenReturn(null);

        // when
        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        // then
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    void saveUserNotAuthenticated_shouldReturnUnauthorizedCode() {
        // given
        long userId = 1L;
        User user = User.builder()
                .id(1L)
                .firstName("Mario")
                .lastName("Rossi")
                .password("password")
                .email("mario@mail.com")
                .build();

        when(userDetails.getUsername()).thenReturn("test@mail.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);

        // when
        ResponseEntity<?> response = userController.save(String.valueOf(userId));

        // then
        assertEquals(401, response.getStatusCodeValue());

    }

    @Test
    void saveUserNotAuthenticated_shouldReturnBadRequestCode() {
        // given
        String userIdString = "nonNumericId";

        // when
        ResponseEntity<?> response = userController.save(userIdString);

        // then
        assertEquals(400, response.getStatusCodeValue());

    }
}