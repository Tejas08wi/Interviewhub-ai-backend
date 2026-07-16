package com.interviewhub.dto.application;

import java.time.LocalDateTime;

import com.interviewhub.enums.ApplicationStatus;

import lombok.Data;

@Data
public class ApplicationResponseDto {

    private Long applicationId;

    private Long jobId;

    private String jobTitle;

    private String companyName;

    private String candidateName;

    private String candidateEmail;

    private ApplicationStatus status;

    private LocalDateTime appliedAt;

}