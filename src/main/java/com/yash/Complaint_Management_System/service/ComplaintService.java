package com.yash.Complaint_Management_System.service;

import com.yash.Complaint_Management_System.dto.ComplaintRequestDto;
import com.yash.Complaint_Management_System.entity.Complaint;
import com.yash.Complaint_Management_System.entity.ComplaintStatus;
import com.yash.Complaint_Management_System.entity.User;
import com.yash.Complaint_Management_System.exception.ResourceNotFoundException;
import com.yash.Complaint_Management_System.exception.UnauthorizedException;
import com.yash.Complaint_Management_System.repository.ComplaintRepository;
import com.yash.Complaint_Management_System.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintService(ComplaintRepository complaintRepository, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    // Create a new complaint for the logged in user
    public Complaint createComplaint(ComplaintRequestDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Complaint complaint = new Complaint();
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setPriority(dto.getPriority());
        complaint.setStatus(ComplaintStatus.OPEN); // always starts as OPEN
        complaint.setUser(user);
        complaint.setCreatedAt(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }

    // Get all complaints (admin)
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // Get complaints belonging to the logged-in user
    public List<Complaint> getMyComplaints(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return complaintRepository.findByUserId(user.getId());
    }

    // Get a single complaint by ID
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));
    }

    public Complaint updateStatus(Long id, ComplaintStatus status) {

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Complaint not found"));

        complaint.setStatus(status);

        return complaintRepository.save(complaint);
    }

    // Filter complaints by status
    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status);
    }

    // Update complaint —> only the owner can update their complaint
    public Complaint updateComplaint(Long id, ComplaintRequestDto dto, String userEmail) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        // Check if the logged-in user owns this complaint
        if (!complaint.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You are not allowed to update this complaint");
        }

        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setPriority(dto.getPriority());

        // Allow status update if provided
        if (dto.getStatus() != null) {
            complaint.setStatus(dto.getStatus());
        }

        return complaintRepository.save(complaint);
    }

    // Delete complaint -> only the owner can delete
    public void deleteComplaint(Long id, String userEmail) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        if (!complaint.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You are not allowed to delete this complaint");
        }

        complaintRepository.delete(complaint);
    }
}