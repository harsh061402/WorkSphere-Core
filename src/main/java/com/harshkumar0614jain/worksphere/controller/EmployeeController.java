package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.model.EmployeeRequestModel;
import com.harshkumar0614jain.worksphere.model.EmployeeResponseModel;
import com.harshkumar0614jain.worksphere.model.EmployeeUpdateRequestModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ResponseModel<EmployeeResponseModel>> createEmployee(
            @Valid @RequestBody EmployeeRequestModel employeeRequest){

        EmployeeResponseModel employeeCreated = employeeService.createEmployee(employeeRequest);
        ResponseModel<EmployeeResponseModel> response = new ResponseModel<>(
                "Employee created successfully", employeeCreated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseModel<List<EmployeeResponseModel>>> getAllEmployees(){

        List<EmployeeResponseModel> userList = employeeService.findAllEmployee();
        ResponseModel<List<EmployeeResponseModel>> response = new ResponseModel<>(
                "Employees retrieved successfully",userList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<EmployeeResponseModel>> getEmployeeById(@PathVariable String id){
        EmployeeResponseModel employee = employeeService.findByEmployeeId(id);
        ResponseModel<EmployeeResponseModel> response = new ResponseModel<>(
                "Employee retrieved successfully",employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseModel<List<EmployeeResponseModel>>> getByDepartment(
            @PathVariable Department department){
        List<EmployeeResponseModel> departmentList = employeeService.findByDepartment(department);
        ResponseModel<List<EmployeeResponseModel>> response = new ResponseModel<>(
                "Employees retrieved successfully",departmentList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseModel<EmployeeResponseModel>> updateEmployee(
            @PathVariable String id,
            @Valid @RequestBody EmployeeUpdateRequestModel employeeUpdateRequest){

        EmployeeResponseModel updatedEmployee = employeeService.updateEmployee(id,employeeUpdateRequest);
        ResponseModel<EmployeeResponseModel> response = new ResponseModel<>(
                "Employee updated successfully",updatedEmployee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<Void>> deleteEmployee(@PathVariable String id){
        employeeService.deleteEmployee(id);
        ResponseModel<Void> response = new ResponseModel<>(
                "Employee deleted successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
