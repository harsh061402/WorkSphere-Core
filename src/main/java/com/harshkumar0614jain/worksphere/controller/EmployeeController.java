package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.enums.Department;
import com.harshkumar0614jain.worksphere.model.EmployeeRequestModel;
import com.harshkumar0614jain.worksphere.model.EmployeeResponseModel;
import com.harshkumar0614jain.worksphere.model.EmployeeUpdateRequestModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Employee Management", description = "APTs for managing employees")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Employee already exists")
    })
    @PostMapping
    public ResponseEntity<ResponseModel<EmployeeResponseModel>> createEmployee(
            @Valid @RequestBody EmployeeRequestModel employeeRequest){

        EmployeeResponseModel employeeCreated = employeeService.createEmployee(employeeRequest);
        ResponseModel<EmployeeResponseModel> response = new ResponseModel<>(
                "Employee created successfully", employeeCreated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ResponseModel<List<EmployeeResponseModel>>> getEmployees(){

        List<EmployeeResponseModel> userList = employeeService.findAllEmployee();
        ResponseModel<List<EmployeeResponseModel>> response = new ResponseModel<>(
                "Employees retrieved successfully",userList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<EmployeeResponseModel>> getEmployeeById(@PathVariable String id){
        EmployeeResponseModel employee = employeeService.findByEmployeeId(id);
        ResponseModel<EmployeeResponseModel> response = new ResponseModel<>(
                "Employee retrieved successfully",employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get employees by department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid department value")
    })
    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseModel<List<EmployeeResponseModel>>> getByDepartment(
            @PathVariable Department department){

        List<EmployeeResponseModel> departmentList = employeeService.findByDepartment(department);
        ResponseModel<List<EmployeeResponseModel>> response = new ResponseModel<>(
                "Employees retrieved successfully",departmentList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @Operation(summary = "Update employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseModel<EmployeeResponseModel>> updateEmployee(
            @PathVariable String id,
            @Valid @RequestBody EmployeeUpdateRequestModel employeeUpdateRequest){

        EmployeeResponseModel updatedEmployee = employeeService.updateEmployee(id,employeeUpdateRequest);
        ResponseModel<EmployeeResponseModel> response = new ResponseModel<>(
                "Employee updated successfully",updatedEmployee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<Void>> deleteEmployee(@PathVariable String id){
        employeeService.deleteEmployee(id);
        ResponseModel<Void> response = new ResponseModel<>(
                "Employee deleted successfully",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
