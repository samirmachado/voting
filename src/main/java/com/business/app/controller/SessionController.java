package com.business.app.controller;

import com.business.app.controller.dto.SessionCreateDto;
import com.business.app.controller.dto.SessionDto;
import com.business.app.controller.dto.SessionResultDto;
import com.business.app.repository.model.Session;
import com.business.app.service.SessionService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/api/v1/session")
@Api(tags = "session")
@Log4j2
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
        log.info("Creating Session: {}", sessionCreateDto);
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
        log.info("List All Sessions");
        Type listType = new TypeToken<List<SessionDto>>(){}.getType();
        return modelMapper.map(sessionService.listAll(), listType);
    }

    @GetMapping("/{sessionId}/result")
    @ApiOperation(value = "get the voting result by the session id", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public SessionResultDto getSessionResult(@PathVariable(required = true) Long sessionId) {
        log.info("Get Session Result of sessionId: {}", sessionId);
        return modelMapper.map(sessionService.getSessionResult(sessionId), SessionResultDto.class);
    }
}
