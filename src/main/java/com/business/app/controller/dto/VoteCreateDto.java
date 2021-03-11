package com.business.app.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoteCreateDto {

    @NotNull(message = "vote is a required field")
    private Boolean vote;

    @NotNull(message = "session is a required field")
    private EntityId session;
}
