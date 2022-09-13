package com.peoples.admin.peoples_admin.dto.response;

import com.peoples.admin.peoples_admin.domain.Admin;
import com.peoples.admin.peoples_admin.domain.enumeration.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SecurityUserResponse {
    private String userId;

    private String name;

    private Role role;

    @Builder
    public SecurityUserResponse(String userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public static SecurityUserResponse of(Admin admin) {
        return SecurityUserResponse.builder()
                .userId(admin.getUserId())
                .name(admin.getNickname())
                .role(admin.getRole())
                .build();
    }
}
