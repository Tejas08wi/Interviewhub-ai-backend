package com.interviewhub.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.interviewhub.dto.recruiter.RecruiterDashboardResponseDto;
import com.interviewhub.dto.recruiter.RecruiterProfileRequestDto;
import com.interviewhub.dto.recruiter.RecruiterProfileResponseDto;

public interface RecruiterService {

    void createRecruiterProfile(
            RecruiterProfileRequestDto request,
            String email);

    void updateRecruiterProfile(
            RecruiterProfileRequestDto request,
            String email);

    RecruiterProfileResponseDto getRecruiterProfile(
            String email);

    void deleteRecruiterProfile(
            String email);

    RecruiterDashboardResponseDto getDashboard(
            String email);

}