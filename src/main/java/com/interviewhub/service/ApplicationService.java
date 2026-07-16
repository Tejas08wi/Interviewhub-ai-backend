package com.interviewhub.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.interviewhub.dto.application.ApplicationRequestDto;
import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.application.ApplicationStatusUpdateRequestDto;
import com.interviewhub.enums.ApplicationStatus;

public interface ApplicationService {

    // Candidate APIs
    void applyForJob(ApplicationRequestDto request, String email);

    List<ApplicationResponseDto> getMyApplications(String email);

    // Recruiter APIs
    List<ApplicationResponseDto> getApplicationsForJob(Long jobId, String email);

    void updateApplicationStatus(
            Long applicationId,
            ApplicationStatusUpdateRequestDto request,
            String email);

    ResponseEntity<Resource> downloadCandidateResume(
            Long applicationId,
            String recruiterEmail);
}