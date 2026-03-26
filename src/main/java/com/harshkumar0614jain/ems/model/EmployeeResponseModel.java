package com.harshkumar0614jain.ems.model;

import com.harshkumar0614jain.ems.entity.Address;
import com.harshkumar0614jain.ems.enums.Department;
import com.harshkumar0614jain.ems.enums.EmployeeStatus;
import com.harshkumar0614jain.ems.enums.Gender;
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
