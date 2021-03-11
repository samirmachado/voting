package com.business.app.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GuidelineCreateDto {

    @NotNull(message = "Description needs to be filled")
    private String description;
}
