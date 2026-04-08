package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.LeaveDecisionRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveResponseModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.LeaveRequestService;
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

@Tag(name = "Leave Request Management", description = "APIs for managing leave requests")
@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Operation(summary = "Apply for a leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave request created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or insufficient balance"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PostMapping
    public ResponseEntity<ResponseModel<LeaveResponseModel>> applyLeaveRequest(
            @Valid @RequestBody LeaveRequestModel requestModel){

        LeaveResponseModel leaveResponseModel = leaveRequestService
                .applyLeave(requestModel);

        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave request created successfully", leaveResponseModel );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all leave requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All leave requests retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ResponseModel<List<LeaveResponseModel>>> getAllLeaveRequests() {

        List<LeaveResponseModel> list = leaveRequestService
                .findAllLeaveRequest();

        ResponseModel<List<LeaveResponseModel>> response = new ResponseModel<>(
                "All leave request retrieved successfully", list);

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @Operation(summary = "Get leave request by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<ResponseModel<LeaveResponseModel>> getLeaveRequestById(
            @PathVariable String leaveRequestId) {
        LeaveResponseModel leaveDetails = leaveRequestService
                .getLeaveDetailsById(leaveRequestId);

        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave details retrieved successfully", leaveDetails);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Get all leave requests by employee ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave requests retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ResponseModel<List<LeaveResponseModel>>> getAllLeaveRequestsByEmployeeId(
            @PathVariable String employeeId) {
        List<LeaveResponseModel> allLeavesOfEmployee = leaveRequestService.findLeaveRequestByEmployeeId(employeeId);
        ResponseModel<List<LeaveResponseModel>> response = new ResponseModel<>(
                "All leave requests retrieved successfully", allLeavesOfEmployee);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Approve or reject a leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status or missing comment"),
            @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    @PatchMapping("/{leaveRequestId}/decision")
    public ResponseEntity<ResponseModel<LeaveResponseModel>> leaveRequestDecision(
            @PathVariable String leaveRequestId,
            @RequestBody @Valid LeaveDecisionRequestModel requestModel) {

        LeaveResponseModel updatedLeave = leaveRequestService
                .approveOrRejectLeave(leaveRequestId, requestModel);

        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave request updated successfully",updatedLeave);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Cancel a leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot cancel rejected or already cancelled leave"),
            @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    @PatchMapping("/{leaveRequestId}/cancel")
    public ResponseEntity<ResponseModel<LeaveResponseModel>> cancelLeaveRequest(
            @PathVariable String leaveRequestId) {

        LeaveResponseModel cancelledLeave = leaveRequestService.cancelLeave(leaveRequestId);
        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave request cancelled successfully", cancelledLeave);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
