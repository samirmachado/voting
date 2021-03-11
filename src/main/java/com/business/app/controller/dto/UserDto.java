package com.business.app.controller.dto;

import com.business.app.controller.dto.constant.RoleDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDto {

    private Long id;
    @NotNull(message = "username is a required field")
    private String username;
    @NotNull(message = "email is a required field")
    private String email;
    @NotNull(message = "cpf is a required field")
    private String cpf;
    @NotNull(message = "User needs at least one role")
    @Size(min = 1)
    List<RoleDto> roles;
}
