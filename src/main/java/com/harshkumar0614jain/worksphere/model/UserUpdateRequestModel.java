package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.Role;
import com.harshkumar0614jain.worksphere.enums.UserStatus;
import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequestModel {

    private String firstName;
    private String lastName;
    private Set<Role> roles;
    private UserStatus status;
}
