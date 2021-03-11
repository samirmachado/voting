package com.business.app.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GuidelineDto {

    private Long id;

    @NotNull(message = "Description needs to be filled")
    private String description;

    private EntityId session;
}
