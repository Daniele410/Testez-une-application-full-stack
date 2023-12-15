package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testAuthenticateUser_shouldReturnResponseBodyOkWhitToken() {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("yourGeneratedJwtToken");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setAdmin(true);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockUser));

        // when
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("yourGeneratedJwtToken", jwtResponse.getToken());

    }

    @Test
    void testRegisterUser_ShouldReturnResponseBodyOk() {
        // given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // when
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof MessageResponse);

        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
    }

    @Test
    void testAuthenticateUser_UserIsNull() {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        UserDetailsImpl userDetails = new UserDetailsImpl(null, "nonexistent@example.com", null, null, null, null);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof JwtResponse);

        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertNotNull(jwtResponse);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void testRegisterUserEmailTaken() {
        // given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // when
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();

        // then
        verify(userRepository, never()).save(any(User.class));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof MessageResponse);
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());
    }
}