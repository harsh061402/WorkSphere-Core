package com.harshkumar0614jain.worksphere.service;

import com.harshkumar0614jain.worksphere.entity.Address;
import com.harshkumar0614jain.worksphere.entity.Employee;
import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.enums.EmployeeStatus;
import com.harshkumar0614jain.worksphere.exception.BusinessException;
import com.harshkumar0614jain.worksphere.exception.ResourceAlreadyExistsException;
import com.harshkumar0614jain.worksphere.exception.ResourceNotFoundException;
import com.harshkumar0614jain.worksphere.model.AddressModel;
import com.harshkumar0614jain.worksphere.model.EmployeeRequestModel;
import com.harshkumar0614jain.worksphere.model.EmployeeResponseModel;
import com.harshkumar0614jain.worksphere.model.EmployeeUpdateRequestModel;
import com.harshkumar0614jain.worksphere.repository.EmployeeRepository;
import com.harshkumar0614jain.worksphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private UserRepository userRepo;

    private EmployeeResponseModel mapToEmployeeResponse(Employee employee){
        return EmployeeResponseModel.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .gender(employee.getGender())
                .mobileNumber(employee.getMobileNumber())
                .employeeStatus(employee.getEmployeeStatus())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .currentAddress(
                        mapToAddressModel(employee.getCurrentAddress()))
                .permanentAddress(
                        mapToAddressModel(employee.getPermanentAddress()))
                .userId(employee.getUserId())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    private Address mapToAddress(AddressModel addressModel){
        return Address.builder()
                .street(addressModel.getStreet())
                .city(addressModel.getCity())
                .state(addressModel.getState())
                .zipCode(addressModel.getZipCode())
                .country(addressModel.getCountry())
                .build();
    }

    private AddressModel mapToAddressModel(Address address){
        return AddressModel.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }

    public EmployeeResponseModel createEmployee(EmployeeRequestModel employeeRequest) {

//        Cross-entity validation
        if(!userRepo.existsById(employeeRequest.getUserId()))
            throw new ResourceNotFoundException("userId","Invalid User Id");

        if(employeeRepo.existsByUserId(employeeRequest.getUserId()))
            throw new ResourceAlreadyExistsException("userId","Employee already exists with this userId");

        if(employeeRepo.existsByMobileNumber(employeeRequest.getMobileNumber()))
            throw new ResourceAlreadyExistsException("mobileNumber","Mobile Number already exists");

        Employee employee = Employee.builder()
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .gender(employeeRequest.getGender())
                .mobileNumber(employeeRequest.getMobileNumber())
                .employeeStatus(EmployeeStatus.ACTIVE)
                .department(employeeRequest.getDepartment())
                .designation(employeeRequest.getDesignation())
                .salary(employeeRequest.getSalary())
                .currentAddress(
                        mapToAddress(employeeRequest.getCurrentAddress()))
                .permanentAddress(
                        mapToAddress(employeeRequest.getPermanentAddress()))
                .userId(employeeRequest.getUserId())
                .build();

        Employee createdEmp = employeeRepo.save(employee);

        return mapToEmployeeResponse(createdEmp);
    }


    public List<EmployeeResponseModel> findAllEmployee() {
        return employeeRepo.findByEmployeeStatusNot(EmployeeStatus.DELETED)
                .stream()
                .map(this::mapToEmployeeResponse)
                .toList();
    }

    public EmployeeResponseModel findByEmployeeId(String id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("employeeId",
                        "Employee not found with id: "+ id));
        return mapToEmployeeResponse(employee);
    }

    public EmployeeResponseModel updateEmployee(String id,EmployeeUpdateRequestModel updateRequest) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee Id",
                        "Employee not found with id: "+ id));

//      Check employee status is not deleted
        if(employee.getEmployeeStatus() == EmployeeStatus.DELETED)
            throw new BusinessException("employeeStatus","Cannot update a deleted employee");

        if(updateRequest.getMobileNumber() != null)
            employee.setMobileNumber(updateRequest.getMobileNumber());

        if(updateRequest.getDepartment() != null)
            employee.setDepartment(updateRequest.getDepartment());

        if(updateRequest.getDesignation() != null)
            employee.setDesignation(updateRequest.getDesignation());

        if(updateRequest.getSalary() != null)
            employee.setSalary(updateRequest.getSalary());

        if (updateRequest.getCurrentAddress() != null)
            employee.setCurrentAddress(mapToAddress(updateRequest.getCurrentAddress()));

        if(updateRequest.getEmployeeStatus() != null)
            employee.setEmployeeStatus(updateRequest.getEmployeeStatus());

        Employee updatedEmp = employeeRepo.save(employee);

        return mapToEmployeeResponse(updatedEmp);
    }

    public void deleteEmployee(String id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Employee Id", "Invalid Employee Id"));

//      Check employee status is already deleted
        if(employee.getEmployeeStatus() == EmployeeStatus.DELETED)
            throw new BusinessException("employeeStatus","Employee is already deleted");
        employee.setEmployeeStatus(EmployeeStatus.DELETED);

        employeeRepo.save(employee);
    }

    public List<EmployeeResponseModel> findByDepartment(Department department) {
        return employeeRepo.findByDepartmentAndEmployeeStatusNot(department,EmployeeStatus.DELETED)
                .stream()
                .map(this::mapToEmployeeResponse)
                .toList();
    }
}
