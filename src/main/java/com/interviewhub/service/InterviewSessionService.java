package com.interviewhub.service;

import com.interviewhub.dto.interview.InterviewRequestDto;
import com.interviewhub.entity.InterviewSession;

public interface InterviewSessionService {

    InterviewSession createInterviewSession(
            String email,
            InterviewRequestDto requestDto
    );

}