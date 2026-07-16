package com.interviewhub.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.interview.InterviewQuestionDto;
import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;
import com.interviewhub.repository.InterviewQuestionRepository;
import com.interviewhub.service.InterviewQuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

    private final InterviewQuestionRepository interviewQuestionRepository;

    @Override
    public List<InterviewQuestion> saveInterviewQuestions(
            InterviewSession session,
            List<InterviewQuestionDto> questions) {

        List<InterviewQuestion> interviewQuestions = questions.stream()
                .map(questionDto -> {

                    InterviewQuestion question = new InterviewQuestion();

                    question.setInterviewSession(session);
                    question.setQuestionNumber(questionDto.getQuestionNumber());
                    question.setCategory(questionDto.getCategory());
                    question.setQuestion(questionDto.getQuestion());
                    question.setExpectedAnswer(questionDto.getExpectedAnswer());

                    return question;

                })
                .toList();

        return interviewQuestionRepository.saveAll(interviewQuestions);
    }
}