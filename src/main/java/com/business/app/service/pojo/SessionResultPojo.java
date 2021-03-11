package com.business.app.service.pojo;

import lombok.Data;

@Data
public class SessionResultPojo {
    private Long yes = 0L;
    private Long no = 0L;
    private Boolean closed;
}
