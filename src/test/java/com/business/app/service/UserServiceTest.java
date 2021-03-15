package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.repository.UserRepository;
import com.business.app.repository.model.User;
import com.business.app.repository.model.constant.Role;
import com.business.app.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest req;

    @Test
    void whenSignUpShouldReturnTheAuthenticationTokenOfTheUserCreated() {
        String username = "username";
        String password = "password";
        String jwtToken = "token";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().username(username).password(password).roles(roles).build();

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded");
        when(userRepository.save(user)).then(returnsFirstArg());
        when(jwtTokenProvider.createToken(username, roles)).thenReturn(jwtToken);

        String returned = userService.signUp(user);

        assertEquals(jwtToken, returned);
    }

    @Test
    void whenSignUpAndTheUserAlreadyExistsShouldReturnCustomException() {
        String username = "username";
        String password = "password";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().username(username).password(password).roles(roles).build();

        when(userRepository.existsByUsername(username)).thenReturn(true);

        Exception exceptionReturned = assertThrows(CustomException.class, () -> {
            userService.signUp(user);
        });

        assertNotNull(exceptionReturned);
    }

    @Test
    void whenDeletingTheUserShouldDeleteTheUserByUsername() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        String username = "username";

        userService.delete(username);

        verify(userRepository).deleteByUsername(arg.capture());

        assertEquals(username, arg.getValue());
    }

    @Test
    void whenSearchingForAUserByUsernameShouldReturnTheUser() {
        String username = "username";
        String password = "password";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().username(username).password(password).roles(roles).build();

        when(userRepository.findByUsername(username)).thenReturn(user);

        User returned = userService.findByUsername(username);

        assertEquals(user, returned);
    }

    @Test
    void whenSearchingForTheUserAndDoesNotExistShouldReturnCustomException() {
        String username = "username";

        when(userRepository.findByUsername(username)).thenReturn(null);

        Exception exceptionReturned = assertThrows(CustomException.class, () -> {
            userService.findByUsername(username);
        });

        assertNotNull(exceptionReturned);
    }

    @Test
    void whenSearchingForTheSessionUserShouldReturnUser() {
        String username = "username";
        String token = "token";
        User user = User.builder().id(1L).build();

        when(jwtTokenProvider.resolveToken(req)).thenReturn(token);
        when(jwtTokenProvider.getUsername(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        User returned = userService.findUserSession(req);

        assertEquals(user, returned);
    }

    @Test
    void whenCreatingTheUserMustReturnTheUser() {
        String username = "username";
        String password = "password";
        String jwtToken = "token";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().username(username).password(password).roles(roles).build();

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded");
        when(userRepository.save(user)).then(returnsFirstArg());

        User returned = userService.create(user);

        assertEquals(user, returned);
    }

    @Test
    void whenCreateUserAndTheUserAlreadyExistsShouldReturnCustomException() {
        String username = "username";
        String password = "password";
        List<Role> roles = new ArrayList(Arrays.asList(Role.ROLE_ADMIN));
        User user = User.builder().username(username).password(password).roles(roles).build();

        when(userRepository.existsByUsername(username)).thenReturn(true);

        Exception exceptionReturned = assertThrows(CustomException.class, () -> {
            userService.create(user);
        });

        assertNotNull(exceptionReturned);
    }

    @Test
    void whenListingAllUsersShouldReturnAListOfUsers() {
        List<User> users = new ArrayList(Arrays.asList(User.builder().id(1L).build(), User.builder().id(2L).build()));

        when(userRepository.findAll()).thenReturn(users);

        List<User> returned = userService.listAll();

        assertEquals(users.size(), returned.size());
    }
}
