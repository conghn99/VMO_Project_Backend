package com.example.vmo_project;

import com.example.vmo_project.request.LoginRequest;
import com.example.vmo_project.response.LoginResponse;
import com.example.vmo_project.security.JwtTokenUtil;
import com.example.vmo_project.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {
    private final Logger logger = LoggerFactory.getLogger(LoginServiceTest.class);

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser() {
        // Arrange
        LoginRequest request = new LoginRequest("user1", "111");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(request.getUsername()))
                .thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails))
                .thenReturn("token");

        // Act
        LoginResponse response = authService.authenticateUser(request);
        logger.info(response.toString());

        // Assert
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertEquals(userDetails, response.getAuth());
        assertEquals("token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(request.getUsername());
        verify(jwtTokenUtil).generateToken(userDetails);
    }

    @Test
    public void testAuthenticateUserWhenAuthenticationFails() {
        // Arrange
        LoginRequest request = new LoginRequest("user", "pass");
        AuthenticationException exception = mock(AuthenticationException.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(exception);

        // Act
        assertThrows(AuthenticationException.class, () -> authService.authenticateUser(request));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtTokenUtil);
    }

    @Test
    public void testAuthenticateUserWhenUserDetailsCannotBeLoaded() {
        // Arrange
        LoginRequest request = new LoginRequest("user", "pass");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(request.getUsername()))
                .thenThrow(UsernameNotFoundException.class);

        // Act
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticateUser(request));

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(request.getUsername());
        verifyNoInteractions(jwtTokenUtil);
    }
}
