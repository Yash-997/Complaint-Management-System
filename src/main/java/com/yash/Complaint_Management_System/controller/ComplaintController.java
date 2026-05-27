package com.yash.Complaint_Management_System.controller;

import com.yash.Complaint_Management_System.dto.ComplaintRequestDto;
import com.yash.Complaint_Management_System.entity.Complaint;
import com.yash.Complaint_Management_System.entity.ComplaintStatus;
import com.yash.Complaint_Management_System.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {

        this.complaintService = complaintService;
    }

    // CREATE COMPLAINT
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(

            @Valid @RequestBody ComplaintRequestDto dto,

            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Complaint complaint = complaintService.createComplaint(
                dto,
                userDetails.getUsername()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(complaint);
    }

    // GET MY COMPLAINTS
    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints(

            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return ResponseEntity.ok(

                complaintService.getMyComplaints(
                        userDetails.getUsername()
                )
        );
    }

    // GET COMPLAINT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                complaintService.getComplaintById(id)
        );
    }

    // FILTER BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Complaint>> getByStatus(

            @PathVariable ComplaintStatus status
    ) {

        return ResponseEntity.ok(

                complaintService.getComplaintsByStatus(status)
        );
    }

    // UPDATE OWN COMPLAINT
    @PutMapping("/{id}")
    public ResponseEntity<Complaint> updateComplaint(

            @PathVariable Long id,

            @Valid @RequestBody ComplaintRequestDto dto,

            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Complaint updated = complaintService.updateComplaint(
                id,
                dto,
                userDetails.getUsername()
        );

        return ResponseEntity.ok(updated);
    }

    // DELETE OWN COMPLAINT
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(

            @PathVariable Long id,

            @AuthenticationPrincipal UserDetails userDetails
    ) {

        complaintService.deleteComplaint(
                id,
                userDetails.getUsername()
        );

        return ResponseEntity.ok(
                "Complaint deleted successfully"
        );
    }
}