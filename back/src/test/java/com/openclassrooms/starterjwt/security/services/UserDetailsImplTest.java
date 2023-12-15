package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void testGetAuthorities_ReturnsEmptySet() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("testuser")
                .lastName("testuser")
                .admin(true)
                .password("openclassrooms")
                .build();

        // when
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testIsAccountNonExpired_AlwaysReturnsTrue() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // when
        boolean result = userDetails.isAccountNonExpired();

        // then
        assertTrue(result);
    }

    @Test
    void testIsAccountNonLocked_AlwaysReturnsTrue() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // when
        boolean result = userDetails.isAccountNonLocked();

        // then
        assertTrue(result);
    }

    @Test
    void testIsCredentialsNonExpired_AlwaysReturnsTrue() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // when
        boolean result = userDetails.isCredentialsNonExpired();

        // then
        assertTrue(result);
    }

    @Test
    void testIsEnabled_AlwaysReturnsTrue() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // when
        boolean result = userDetails.isEnabled();

        // then
        assertTrue(result);
    }

    @Test
    void testEquals_WithEqualObjects_ReturnsTrue() {
        // given
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(1L).build();

        // when
        boolean result = userDetails1.equals(userDetails2);

        // then
        assertTrue(result);
    }

    @Test
    void testEquals_WithDifferentObjects_ReturnsFalse() {
        // given
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(2L).build();

        // when
        boolean result = userDetails1.equals(userDetails2);

        // then
        assertFalse(result);
    }

    @Test
    void testEquals_WithNullObject_ReturnsFalse() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        // when
        boolean result = userDetails.equals(null);

        // then
        assertFalse(result);
    }

    @Test
    void testEquals_WithDifferentClassObject_ReturnsFalse() {
        // given
        UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).build();

        // when
        boolean result = userDetails.equals(new Object());

        // then
        assertFalse(result);
    }

}