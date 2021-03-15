package com.business.app.service;

import com.business.app.exception.CustomException;
import com.business.app.repository.UserRepository;
import com.business.app.repository.model.User;
import com.business.app.security.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Log4j2
public class UserService {

    public static final String USERNAME_IS_ALREADY_IN_USE = "Username is already in use";
    public static final String THE_USER_DOESN_T_EXIST = "The user doesn't exist";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signUp(User user) {
        log.info("SignUp user: {}", user);
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("User created!");
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new CustomException(USERNAME_IS_ALREADY_IN_USE, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String username) {
        log.info("Deleting user by username: {}", username);
        userRepository.deleteByUsername(username);
    }

    public User findByUsername(String username) {
        log.info("Searching user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(THE_USER_DOESN_T_EXIST, HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public User findUserSession(HttpServletRequest req) {
        log.info("Searching logged user");
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public User create(User user) {
        log.info("Creating user: {}", user);
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } else {
            throw new CustomException(USERNAME_IS_ALREADY_IN_USE, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<User> listAll() {
        log.info("Listing Users");
        return userRepository.findAll();
    }
}
