package com.business.app.controller;

import com.business.app.controller.dto.JwtTokenDto;
import com.business.app.controller.dto.UserCreateDto;
import com.business.app.controller.dto.UserDto;
import com.business.app.repository.model.User;
import com.business.app.service.UserService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
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
        return modelMapper.map(userService.findUserSession(req), UserDto.class);
    }

}
