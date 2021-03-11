package com.business.app.controller;

import com.business.app.controller.dto.SessionCreateDto;
import com.business.app.controller.dto.SessionDto;
import com.business.app.repository.model.Session;
import com.business.app.service.SessionService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/session")
@Api(tags = "session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ApiOperation(value = "create a session to voting", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SessionDto create(@RequestBody(required = true) SessionCreateDto sessionCreateDto) {
        Session createdSession = sessionService.create(modelMapper.map(sessionCreateDto, Session.class));
        return modelMapper.map(createdSession, SessionDto.class);
    }

    @GetMapping
    @ApiOperation(value = "list all sessions", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SessionDto> listAll() {
        Type listType = new TypeToken<List<SessionDto>>(){}.getType();
        return modelMapper.map(sessionService.listAll(), listType);
    }
}