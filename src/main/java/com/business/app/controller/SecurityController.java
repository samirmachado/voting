package com.business.app.controller;

import com.business.app.controller.dto.JwtTokenDto;
import com.business.app.controller.dto.SignInDto;
import com.business.app.service.SecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/security")
@Api(tags = "security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signin")
    @ApiOperation(value = "Authenticates user and returns its JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public JwtTokenDto signIn(@RequestBody(required = true) SignInDto signInDto) {
        String token = securityService.signIn(signInDto.getUsername(), signInDto.getPassword());
        return JwtTokenDto.builder().token(token).build();
    }

    @GetMapping("/refresh-token")
    @ApiOperation(value = "Refresh JWT token")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ASSOCIATE')")
    public JwtTokenDto refresh(HttpServletRequest req) {
        String token = securityService.refresh(req.getRemoteUser());
        return JwtTokenDto.builder().token(token).build();
    }

}
