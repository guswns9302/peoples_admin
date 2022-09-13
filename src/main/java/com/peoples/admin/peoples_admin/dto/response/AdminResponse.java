package com.peoples.admin.peoples_admin.dto.response;

import com.peoples.admin.peoples_admin.domain.Admin;
import com.peoples.admin.peoples_admin.domain.enumeration.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@ToString
public class AdminResponse {

    private String userId;
    private String nickname;
    private Role role;
    private String createdAt;
    private String lastLoginAt;

    public static AdminResponse from(Admin admin){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String lastLogin = "";
        if(admin.getLastLoginAt() != null){
            lastLogin = admin.getLastLoginAt().format(formatter);
        }
        return AdminResponse.builder()
                .userId(admin.getUserId())
                .nickname(admin.getNickname())
                .role(admin.getRole())
                .createdAt(admin.getCreatedAt().format(formatter))
                .lastLoginAt(lastLogin)
                .build();
    }
}
