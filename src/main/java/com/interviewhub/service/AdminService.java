package com.interviewhub.service;

import java.util.List;

import com.interviewhub.dto.admin.AdminCandidateListResponseDto;
import com.interviewhub.dto.admin.AdminDashboardResponseDto;
import com.interviewhub.dto.admin.AdminInterviewDetailsResponseDto;
import com.interviewhub.dto.admin.AdminRecruiterListResponseDto;
import com.interviewhub.dto.application.ApplicationResponseDto;
import com.interviewhub.dto.candidate.CandidateProfileResponseDto;
import com.interviewhub.dto.interview.InterviewHistoryResponseDto;
import com.interviewhub.dto.job.JobResponseDto;
import com.interviewhub.dto.recruiter.RecruiterProfileResponseDto;

public interface AdminService {

    AdminDashboardResponseDto getDashboard();

    List<AdminCandidateListResponseDto> getAllCandidates();

    CandidateProfileResponseDto getCandidateById(Long candidateId);

    void deleteCandidate(Long candidateId);

    List<AdminRecruiterListResponseDto> getAllRecruiters();

    RecruiterProfileResponseDto getRecruiterById(Long recruiterId);

    void deleteRecruiter(Long recruiterId);

    List<JobResponseDto> getAllJobs();

    JobResponseDto getJobById(Long jobId);

    void deleteJob(Long jobId);

    List<ApplicationResponseDto> getAllApplications();

    ApplicationResponseDto getApplicationById(Long applicationId);

    void deleteApplication(Long applicationId);

    List<InterviewHistoryResponseDto> getAllInterviews();

    AdminInterviewDetailsResponseDto getInterviewById(Long sessionId);

    void deleteInterview(Long sessionId);

}