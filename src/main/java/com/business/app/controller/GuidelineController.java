package com.business.app.controller;

import com.business.app.controller.dto.GuidelineCreateDto;
import com.business.app.controller.dto.GuidelineDto;
import com.business.app.repository.model.Guideline;
import com.business.app.service.GuidelineService;
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
@RequestMapping("/api/v1/guideline")
@Api(tags = "guideline")
@Log4j2
public class GuidelineController {

    @Autowired
    private GuidelineService guidelineService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ApiOperation(value = "create a guideline", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GuidelineDto create(@RequestBody(required = true) GuidelineCreateDto guidelineCreateDto) {
        log.info("Creating Guideline: {}", guidelineCreateDto);
        Guideline createdGuideline = guidelineService.create(modelMapper.map(guidelineCreateDto, Guideline.class));
        return modelMapper.map(createdGuideline, GuidelineDto.class);
    }

    @GetMapping
    @ApiOperation(value = "list all guidelines", authorizations = {@Authorization(value = "apiKey")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Doesn't exist"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GuidelineDto> listAll() {
        log.info("List All Guidelines");
        Type listType = new TypeToken<List<GuidelineDto>>(){}.getType();
        return modelMapper.map(guidelineService.listAll(), listType);
    }
}
