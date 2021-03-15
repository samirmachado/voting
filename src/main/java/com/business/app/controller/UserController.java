package com.business.app.controller;

import com.business.app.controller.dto.JwtTokenDto;
import com.business.app.controller.dto.UserCreateDto;
import com.business.app.controller.dto.UserDto;
import com.business.app.repository.model.User;
import com.business.app.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "users")
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signup")
    @ApiOperation(value = "Creates user and returns its JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public JwtTokenDto signUp(@RequestBody(required = true) UserCreateDto userCreateDto) {
        log.info("SignUp: {}", userCreateDto);
        String token = userService.signUp(modelMapper.map(userCreateDto, User.class));
        return JwtTokenDto.builder().token(token).build();
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Deletes specific user by username", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void delete(@PathVariable(required = true) String username) {
        log.info("Deleting user by username: {}", username);
        userService.delete(username);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Create a new user", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserDto create(@RequestBody(required = true) UserCreateDto userCreateDto) {
        log.info("Creating user: {}", userCreateDto);
        User createdUser = userService.create(modelMapper.map(userCreateDto, User.class));
        return modelMapper.map(createdUser, UserDto.class);
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Returns specific user by username", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserDto search(@PathVariable(required = true) String username) {
        log.info("Searching user by username: {}", username);
        return modelMapper.map(userService.findByUsername(username), UserDto.class);
    }

    @GetMapping
    @ApiOperation(value = "list all users", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDto> listAll() {
        log.info("List All Users");
        Type listType = new TypeToken<List<UserDto>>(){}.getType();
        return modelMapper.map(userService.listAll(), listType);
    }

    @GetMapping(value = "/get-my-user")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSOCIATE')")
    @ApiOperation(value = "Returns current user's data", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserDto findUserSession(HttpServletRequest req) {
        log.info("Searching logged user");
        UserDto user = modelMapper.map(userService.findUserSession(req), UserDto.class);
        log.info("Logged user id: {}", user);
        return user;
    }

}
