package com.yash.Complaint_Management_System.controller;

import com.yash.Complaint_Management_System.entity.Complaint;
import com.yash.Complaint_Management_System.entity.ComplaintStatus;
import com.yash.Complaint_Management_System.service.ComplaintService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final ComplaintService complaintService;

    public AdminController(ComplaintService complaintService) {

        this.complaintService = complaintService;
    }

    // GET ALL COMPLAINTS
    @GetMapping("/complaints")
    public ResponseEntity<List<Complaint>> getAllComplaints() {

        return ResponseEntity.ok(
                complaintService.getAllComplaints()
        );
    }

    // UPDATE COMPLAINT STATUS
    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<Complaint> updateComplaintStatus(

            @PathVariable Long id,

            @RequestParam ComplaintStatus status
    ) {

        return ResponseEntity.ok(
                complaintService.updateStatus(id, status)
        );
    }
}