package com.business.app.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SessionCreateDto {

    private LocalDateTime expirationDate;

    @NotNull(message = "To create a session, there must be an associated Guideline")
    private EntityId guideline;
}
