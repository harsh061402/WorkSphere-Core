package com.harshkumar0614jain.ems.model;

import com.harshkumar0614jain.ems.entity.Address;
import com.harshkumar0614jain.ems.entity.User;
import com.harshkumar0614jain.ems.enums.Department;
import com.harshkumar0614jain.ems.enums.EmployeeStatus;
import com.harshkumar0614jain.ems.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
public class EmployeeRequestModel {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(
            regexp = "^(\\+91[ ]?)?[6-9][0-9]{4}[0-9 ]{5,6}$",
            message = "Invalid Indian mobile number"
    )
    private String mobileNumber;

    @NotNull(message = "Department is required")
    private Department department;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotNull(message = "Salary is required")
    @Min(value = 0,message = "Salary must be positive")
    private Long salary;

    @NotNull(message = "Current Address is required")
    private AddressModel currentAddress;

    @NotNull(message = "Permanent Address is required")
    private AddressModel permanentAddress;

    @NotNull(message = "User Id is required")
    private String userId;
}
