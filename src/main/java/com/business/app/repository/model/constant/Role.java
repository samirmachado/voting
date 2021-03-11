package com.business.app.repository.model.constant;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_ASSOCIATE;

    public String getAuthority() {
        return name();
    }

}
