package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    @Mock
    private UserDetailsImpl userPrincipal;

    @Mock
    private Authentication authentication;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Disabled
    @Test
    void generateJwtToken_ValidAuthentication_GeneratesToken() {
        // Mock UserDetails
        UserDetails userDetails = new UserDetailsImpl(1L, "testuser", "testuser", "testuser", true, "testuser");
        when(userPrincipal.getUsername()).thenReturn(userDetails.getUsername());
        when(authentication.getPrincipal()).thenReturn(userDetails);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        when(userPrincipal.getUsername()).thenReturn("testuser");
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Test generateJwtToken
        String token = jwtUtils.generateJwtToken(authentication);

        // Validate token
        String expectedToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertEquals(expectedToken, token);
    }
}