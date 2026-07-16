package com.interviewhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interviewhub.entity.Job;
import com.interviewhub.entity.JobApplication;
import com.interviewhub.entity.User;

@Repository
public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Check whether candidate already applied for a job
    boolean existsByCandidateAndJob(User candidate, Job job);

    // Candidate - View My Applications
    List<JobApplication> findByCandidate(User candidate);

    // Recruiter - View Applications for a Job
    List<JobApplication> findByJob(Job job);

    // Find application by ID
    Optional<JobApplication> findById(Long id);

    void deleteByCandidate(User candidate); 

    void deleteByJob(Job job);

}