package com.business.app.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {

    private String token;
}
