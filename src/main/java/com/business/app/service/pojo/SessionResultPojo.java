package com.business.app.service.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionResultPojo {
    private Long yes = 0L;
    private Long no = 0L;
    private Boolean closed;
}
