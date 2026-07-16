package com.interviewhub.service;

import com.interviewhub.dto.interview.InterviewEvaluationResponseDto;

public interface InterviewEvaluationService {

    InterviewEvaluationResponseDto evaluateInterview(Long sessionId);

    InterviewEvaluationResponseDto getEvaluation(Long sessionId);

}