package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.repository.UserRepository;
import com.business.app.repository.model.User;
import com.business.app.repository.model.constant.Role;
import com.business.app.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void whenSignInShouldCreateTheJWTToken() {
        String username = "username";
        String password = "password";
        String jwtToken = "token";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().roles(roles).build();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(jwtTokenProvider.createToken(username, roles)).thenReturn(jwtToken);

        String returned = securityService.signIn(username, password);

        assertEquals(jwtToken, returned);
    }

    @Test
    void whenSignInAndAuthenticationFailsShouldReturnCustomException() {

        String username = "username";
        String password = "password";
        Exception ex = new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenThrow(ex);

        Exception exception = assertThrows(CustomException.class, () -> {
            securityService.signIn(username, password);
        });

        assertEquals(ex, exception);
    }

    @Test
    void whenRefreshTokenShouldReturnTheNewToken() {

        String username = "username";
        String jwtToken = "token";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().roles(roles).build();

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(jwtTokenProvider.createToken(username, roles)).thenReturn(jwtToken);

        String returned = securityService.refresh(username);

        assertEquals(jwtToken, returned);
    }

}
