package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userPrincipal;
    @InjectMocks
    private JwtUtils jwtUtils;
    private String jwtSecret = "openclassrooms";
    private int jwtExpirationMs = 3600000;

    @Test
    void generateJwtToken_ValidAuthentication_GeneratesToken() {
        // given
        UserDetails userDetails = new UserDetailsImpl(1L, "testuser", "testuser", "testuser", true, "openclassrooms");

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn(userDetails.getUsername());

        // when
        String token = jwtUtils.generateJwtToken(authentication);

        // then
        String expectedToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertEquals(expectedToken.split("\\.")[0], token.split("\\.")[0]);
    }

    @Test
    void getUserNameFromJwtToken_ValidToken_ReturnsUsername() {
        // given
        String username = "testuser";
        String token = generateValidToken(username);

        // when
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // then
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateJwtToken_ValidToken_ReturnsTrue() {
        // given
        String validToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Un'ora in millisecondi
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // when
        boolean isValid = jwtUtils.validateJwtToken(validToken);

        // then
        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_InvalidSignature() {
        // given
        String invalidSignatureToken = generateValidToken() + "tampered";

        // when
        boolean isValid = jwtUtils.validateJwtToken(invalidSignatureToken);

        // then
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_InvalidToken() {
        // given
        String invalidToken = "invalidToken";

        // when
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // then
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_ExpiredToken() {
        // given
        String expiredToken = generateExpiredToken();

        // when
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // then
        assertFalse(isValid);

    }

    @Test
    void validateJwtToken_UnsupportedToken() {
        // given
        String unsupportedToken = generateUnsupportedToken();

        // when
        boolean isValid = jwtUtils.validateJwtToken(unsupportedToken);

        // then
        assertFalse(isValid);

    }

    @Test
    void validateJwtToken_EmptyClaims() {
        // given
        String emptyClaimsToken = Jwts.builder()
                .setSubject("testuser")
                .signWith(SignatureAlgorithm.HS512, "your_secret_key")
                .compact();

        // when
        boolean isValid = jwtUtils.validateJwtToken(emptyClaimsToken);

        // then
        assertFalse(isValid);
    }

    private String generateValidToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateUnsupportedToken() {
        return Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }


    private String generateValidToken() {
        return Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

    private String generateExpiredToken() {
        return Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

}