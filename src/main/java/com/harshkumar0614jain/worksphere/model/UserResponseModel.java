package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.Role;
import com.harshkumar0614jain.worksphere.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class UserResponseModel {
    private String id;
    private String username;
    private String email;
    private Set<Role> roles;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginDate;
}
