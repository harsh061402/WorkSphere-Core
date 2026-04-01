package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.LeaveAllocationRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveAllocationResponseModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.LeaveAllocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-allocations")
public class LeaveAllocationController {

    @Autowired
    private LeaveAllocationService leaveAllocationService;

    @PostMapping
    public ResponseEntity<ResponseModel<LeaveAllocationResponseModel>> allocateLeaveEmployee(
            @RequestBody @Valid LeaveAllocationRequestModel requestModel){
        LeaveAllocationResponseModel allocationResponseModel = leaveAllocationService.allocateLeave(requestModel);
        ResponseModel<LeaveAllocationResponseModel> response= new ResponseModel<>(
                "Leave allocated successfully", allocationResponseModel);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseModel<List<LeaveAllocationResponseModel>>> getAllAllocateLeaveByEmployeeId(
            @PathVariable String employeeId){

        List<LeaveAllocationResponseModel> allAllocateLeave = leaveAllocationService.getAllocationByEmployee(employeeId);
        ResponseModel<List<LeaveAllocationResponseModel>> response= new ResponseModel<>(
                "Leave allocations retrieved successfully", allAllocateLeave);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
