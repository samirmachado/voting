package com.business.app.controller.dto;

import com.business.app.controller.dto.constant.SessionStatusDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SessionDto {

    private Long id;

    private LocalDateTime expirationDate;

    private SessionStatusDto sessionStatus;

    @NotNull(message = "To create a session, there must be an associated Guideline")
    private EntityId guideline;
}
