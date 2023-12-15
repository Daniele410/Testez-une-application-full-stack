package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Test
    public void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("username");
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(userDetailsService).loadUserByUsername("username");
        verify(userDetails).getAuthorities();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalWithException() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtils.validateJwtToken("validToken")).thenThrow(new RuntimeException("Simulated exception"));

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtUtils.validateJwtToken("invalidToken")).thenReturn(false);

        // when
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
    }

}