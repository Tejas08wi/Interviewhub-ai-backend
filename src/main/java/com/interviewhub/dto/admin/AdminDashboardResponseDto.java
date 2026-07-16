package com.interviewhub.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponseDto {

    private long totalUsers;

    private long totalCandidates;

    private long totalRecruiters;

    private long totalJobs;

    private long totalApplications;

    private long totalInterviewSessions;

    private long totalResumes;

}