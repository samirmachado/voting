package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.repository.UserRepository;
import com.business.app.security.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SecurityService {

    public static final String INVALID_USERNAME_PASSWORD_SUPPLIED = "Invalid username/password supplied";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signIn(String username, String password) {
        log.info("SignIn, username: {}", username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException(INVALID_USERNAME_PASSWORD_SUPPLIED, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String refresh(String username) {
        log.info("Refresh token, username: {}", username);
        return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }
}
