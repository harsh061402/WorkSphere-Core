package com.harshkumar0614jain.ems.entity;

import com.harshkumar0614jain.ems.model.LeaveBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "leave_allocations")
@Data
@AllArgsConstructor
public class LeaveAllocation {

    @Id
    private String id;

    private String employeeId;
    private int year;

    private List<LeaveBalance> leaveBalances;

//    @Id private String id;
//    private String employeeId;
//    private LeaveType leaveType;
//    private int totalLeaves;
//    private int usedLeaves;
//    private int remainingLeaves; // or compute it: totalLeaves - usedLeaves
//    private int year;


}
