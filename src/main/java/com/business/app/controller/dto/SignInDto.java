package com.business.app.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignInDto {

    @NotNull(message = "username is a required field")
    private String username;
    @NotNull(message = "password is a required field")
    private String password;
}
