package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.enums.EmployeeStatus;
import com.harshkumar0614jain.worksphere.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EmployeeResponseModel {
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String mobileNumber;
    private EmployeeStatus employeeStatus;
    private Department department;
    private String designation;
    private Long salary;
    private AddressModel currentAddress;
    private AddressModel permanentAddress;
    private String userId;
    private Instant createdAt;
    private Instant updatedAt;
}
