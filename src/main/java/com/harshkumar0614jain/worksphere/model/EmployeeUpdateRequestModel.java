package com.harshkumar0614jain.worksphere.model;

import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.enums.EmployeeStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeUpdateRequestModel {

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+91[ ]?)?[6-9][0-9]{4}[0-9 ]{5,6}$",
            message = "Invalid Indian mobile number")
    private String mobileNumber;

    private EmployeeStatus employeeStatus;

    private Department department;

    private String designation;

    @Min(value = 0,message = "Salary must be positive")
    private Long salary;

    private AddressModel currentAddress;
}
