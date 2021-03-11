package com.business.app.controller.dto.constant;

import org.springframework.security.core.GrantedAuthority;

public enum RoleDto implements GrantedAuthority {
    ROLE_ADMIN, ROLE_ASSOCIATE;

    public String getAuthority() {
        return name();
    }

}
