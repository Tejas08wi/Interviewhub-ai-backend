package com.interviewhub.service;

import java.util.List;

import com.interviewhub.dto.interview.InterviewQuestionDto;
import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;

public interface InterviewQuestionService {

    List<InterviewQuestion> saveInterviewQuestions(
            InterviewSession session,
            List<InterviewQuestionDto> questions);

}