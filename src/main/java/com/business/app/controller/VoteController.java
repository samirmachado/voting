package com.business.app.controller;

import com.business.app.controller.dto.VoteCreateDto;
import com.business.app.controller.dto.VoteDto;
import com.business.app.repository.model.User;
import com.business.app.repository.model.Vote;
import com.business.app.service.UserService;
import com.business.app.service.VoteService;
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
@RequestMapping("/vote")
@Api(tags = "vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ApiOperation(value = "Performs the vote in the session", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "The session does not exist or has already been closed"),
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ASSOCIATE')")
    public void vote(@RequestBody(required = true) VoteCreateDto voteCreateDto, HttpServletRequest req) {
        User user = userService.findUserSession(req);
        Vote vote = modelMapper.map(voteCreateDto, Vote.class);
        vote.setUser(user);
        voteService.vote(vote);
    }

    @GetMapping
    @ApiOperation(value = "list all votes", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VoteDto> listAll() {
        Type listType = new TypeToken<List<VoteDto>>(){}.getType();
        return modelMapper.map(voteService.listAll(), listType);
    }
}
