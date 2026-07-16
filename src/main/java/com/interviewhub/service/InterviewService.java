package com.interviewhub.service;

import com.interviewhub.dto.interview.InterviewHistoryResponseDto;
import com.interviewhub.dto.interview.InterviewRequestDto;
import com.interviewhub.dto.interview.InterviewResponseDto;
import java.util.List;

public interface InterviewService {

    InterviewResponseDto generateInterviewQuestions(
            String email,
            InterviewRequestDto requestDto);

    List<InterviewHistoryResponseDto> getInterviewHistory(String email);

}