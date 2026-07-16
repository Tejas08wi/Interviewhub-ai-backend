package com.interviewhub.service;

import java.util.List;

import com.interviewhub.dto.job.JobRequestDto;
import com.interviewhub.dto.job.JobResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {

    // Recruiter APIs
    void createJob(JobRequestDto request, String email);

    List<JobResponseDto> getAllJobs(String email);

    JobResponseDto getJobById(Long jobId, String email);

    void updateJob(Long jobId, JobRequestDto request, String email);

    void deleteJob(Long jobId, String email);

    // Candidate APIs
    List<JobResponseDto> getAllActiveJobs();

    JobResponseDto getActiveJobById(Long jobId);

    List<JobResponseDto> searchJobs(
            String title,
            String skills,
            String location,
            String experience);

    Page<JobResponseDto> getRecruiterJobs(
            String email,
            Pageable pageable);

    Page<JobResponseDto> getCandidateJobs(
            Pageable pageable);
}