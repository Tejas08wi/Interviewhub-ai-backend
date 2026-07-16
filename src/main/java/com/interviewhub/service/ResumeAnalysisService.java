package com.interviewhub.service;

import com.interviewhub.dto.ai.ResumeAnalysisResponseDto;

public interface ResumeAnalysisService {

    ResumeAnalysisResponseDto analyzeResume(String email);

}