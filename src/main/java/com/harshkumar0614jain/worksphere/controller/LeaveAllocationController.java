package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.LeaveAllocationRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveAllocationResponseModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.LeaveAllocationService;
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

@Tag(name = "Leave Allocation Management", description = "APIs for managing leave allocations")
@RestController
@RequestMapping("/api/leave-allocations")
public class LeaveAllocationController {

    @Autowired
    private LeaveAllocationService leaveAllocationService;

    @Operation(summary = "Allocate leaves to an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave allocated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Leave allocation already exists")
    })
    @PostMapping
    public ResponseEntity<ResponseModel<LeaveAllocationResponseModel>> allocateLeaveEmployee(
            @RequestBody @Valid LeaveAllocationRequestModel requestModel){
        LeaveAllocationResponseModel allocationResponseModel = leaveAllocationService.allocateLeave(requestModel);
        ResponseModel<LeaveAllocationResponseModel> response= new ResponseModel<>(
                "Leave allocated successfully", allocationResponseModel);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all leave allocations by employee ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave allocations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseModel<List<LeaveAllocationResponseModel>>> getAllAllocateLeaveByEmployeeId(
            @PathVariable String employeeId){

        List<LeaveAllocationResponseModel> allAllocateLeave = leaveAllocationService.getAllocationByEmployee(employeeId);
        ResponseModel<List<LeaveAllocationResponseModel>> response= new ResponseModel<>(
                "Leave allocations retrieved successfully", allAllocateLeave);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
