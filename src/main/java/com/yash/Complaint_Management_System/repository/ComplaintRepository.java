package com.yash.Complaint_Management_System.repository;

import com.yash.Complaint_Management_System.entity.Complaint;
import com.yash.Complaint_Management_System.entity.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Get all complaints by a specific user
    List<Complaint> findByUserId(Long userId);

    // Filter complaints by status
    List<Complaint> findByStatus(ComplaintStatus status);

    // Filter complaints by user and status
    List<Complaint> findByUserIdAndStatus(Long userId, ComplaintStatus status);
}