package com.business.app.integration.dto;

import com.business.app.integration.dto.constant.CpfValidadeStatusDto;
import lombok.Setter;

@Setter
public class CpfValidateDto {
    private CpfValidadeStatusDto status;

    public boolean isValid() {
        return status.equals(CpfValidadeStatusDto.ABLE_TO_VOTE);
    }
}
