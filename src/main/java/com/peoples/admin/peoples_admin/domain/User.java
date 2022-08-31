package com.peoples.admin.peoples_admin.domain;

import com.peoples.admin.peoples_admin.domain.enumeration.Role;
import lombok.*;
import java.time.LocalDateTime;

@Getter
//@Entity
@Builder
@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "TB_USER")
public class User {
    //@Id
    //@Column(name = "USER_ID")
    private String userId;

    //@Column(name = "PASSWORD")
    private String password;

    //@Column(name = "NICKNAME")
    private String nickname;

    //@Column(name = "ROLE")
    //@Enumerated(EnumType.STRING)
    private Role role;

    //@CreatedDate
    //@Column(name = "CREATED_AT", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    //@LastModifiedDate
    //@Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    //@Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLoginAt;
}
