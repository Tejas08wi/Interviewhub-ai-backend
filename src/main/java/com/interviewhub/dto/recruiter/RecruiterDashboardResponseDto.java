package com.interviewhub.dto.recruiter;

import lombok.Data;

@Data
public class RecruiterDashboardResponseDto {

    private String recruiterName;

    private String email;

    private String companyName;

    private String designation;

    private Long totalCandidates;

    private Long totalInterviewSessions;

    private Long completedEvaluations;

    private Double averageInterviewScore;

}