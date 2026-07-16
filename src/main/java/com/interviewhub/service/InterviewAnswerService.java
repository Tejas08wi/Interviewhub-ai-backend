package com.interviewhub.service;

import com.interviewhub.dto.interview.InterviewSubmissionResponseDto;
import com.interviewhub.dto.interview.SubmitInterviewRequestDto;

public interface InterviewAnswerService {

    InterviewSubmissionResponseDto submitInterview(
        String email,
        SubmitInterviewRequestDto requestDto
);

}