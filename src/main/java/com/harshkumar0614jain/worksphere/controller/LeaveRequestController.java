package com.harshkumar0614jain.worksphere.controller;

import com.harshkumar0614jain.worksphere.model.LeaveDecisionRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveRequestModel;
import com.harshkumar0614jain.worksphere.model.LeaveResponseModel;
import com.harshkumar0614jain.worksphere.model.ResponseModel;
import com.harshkumar0614jain.worksphere.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<ResponseModel<LeaveResponseModel>> applyLeaveRequest(
            @Valid @RequestBody LeaveRequestModel requestModel){

        LeaveResponseModel leaveResponseModel = leaveRequestService
                .applyLeave(requestModel);

        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave request created successfully", leaveResponseModel );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseModel<List<LeaveResponseModel>>> getAllLeaveRequests() {

        List<LeaveResponseModel> list = leaveRequestService
                .findAllLeaveRequest();

        ResponseModel<List<LeaveResponseModel>> response = new ResponseModel<>(
                "All leave request retrieved successfully", list);

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<ResponseModel<LeaveResponseModel>> getLeaveRequestById(
            @PathVariable String leaveRequestId) {
        LeaveResponseModel leaveDetails = leaveRequestService
                .getLeaveDetailsById(leaveRequestId);

        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave details retrieved successfully", leaveDetails);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ResponseModel<List<LeaveResponseModel>>> getAllLeaveRequestsByEmployeeId(
            @PathVariable String employeeId) {
        List<LeaveResponseModel> allLeavesOfEmployee = leaveRequestService.findLeaveRequestByEmployeeId(employeeId);
        ResponseModel<List<LeaveResponseModel>> response = new ResponseModel<>(
                "All leave requests retrieved successfully", allLeavesOfEmployee);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

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

    @PatchMapping("/{leaveRequestId}/cancel")
    public ResponseEntity<ResponseModel<LeaveResponseModel>> cancelLeaveRequest(
            @PathVariable String leaveRequestId) {

        LeaveResponseModel cancelledLeave = leaveRequestService.cancelLeave(leaveRequestId);
        ResponseModel<LeaveResponseModel> response = new ResponseModel<>(
                "Leave request cancelled successfully", cancelledLeave);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
