package com.business.app.integration.dto;

import com.business.app.integration.dto.constant.CpfValidadeStatusDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CpfValidateDto {
    private CpfValidadeStatusDto status;

    public boolean isValid() {
        return status.equals(CpfValidadeStatusDto.ABLE_TO_VOTE);
    }
}
