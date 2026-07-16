package com.interviewhub.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.interview.InterviewAnswerRequestDto;
import com.interviewhub.dto.interview.SubmitInterviewRequestDto;
import com.interviewhub.entity.InterviewAnswer;
import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;
import com.interviewhub.entity.User;
import com.interviewhub.exception.BadRequestException;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.InterviewAnswerRepository;
import com.interviewhub.repository.InterviewQuestionRepository;
import com.interviewhub.repository.InterviewSessionRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.InterviewAnswerService;
import com.interviewhub.service.InterviewEvaluationService;

import lombok.RequiredArgsConstructor;
import com.interviewhub.dto.interview.InterviewSubmissionResponseDto;
import com.interviewhub.dto.interview.InterviewEvaluationResponseDto;
import com.interviewhub.service.InterviewEvaluationService;

@Service
@RequiredArgsConstructor
public class InterviewAnswerServiceImpl implements InterviewAnswerService {

        private final InterviewAnswerRepository interviewAnswerRepository;
        private final InterviewQuestionRepository interviewQuestionRepository;
        private final InterviewSessionRepository interviewSessionRepository;
        private final UserRepository userRepository;
        private final InterviewEvaluationService interviewEvaluationService;

        @Override
        public InterviewSubmissionResponseDto submitInterview(
                        String email,
                        SubmitInterviewRequestDto requestDto) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                InterviewSession session = interviewSessionRepository.findById(requestDto.getSessionId())
                                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found"));

                if (!session.getUser().getId().equals(user.getId())) {
                        throw new BadRequestException("You are not authorized to submit this interview.");
                }

                List<InterviewAnswer> answers = new ArrayList<>();

                for (InterviewAnswerRequestDto answerDto : requestDto.getAnswers()) {

                        InterviewQuestion question = interviewQuestionRepository
                                        .findByIdAndInterviewSession(answerDto.getQuestionId(), session)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Interview question not found"));

                        if (interviewAnswerRepository.findByInterviewQuestion(question).isPresent()) {
                                throw new BadRequestException(
                                                "Answer already submitted for question ID: " + question.getId());
                        }

                        InterviewAnswer answer = new InterviewAnswer();

                        answer.setInterviewQuestion(question);
                        answer.setCandidateAnswer(answerDto.getCandidateAnswer());

                        answers.add(answer);
                }

                interviewAnswerRepository.saveAll(answers);

                // Automatically evaluate interview
                InterviewEvaluationResponseDto evaluation = interviewEvaluationService
                                .evaluateInterview(session.getId());

                return InterviewSubmissionResponseDto.builder()
                                .sessionId(session.getId())
                                .totalAnswersSubmitted(answers.size())
                                .message("Interview submitted successfully.")
                                .evaluation(evaluation)
                                .build();
        }
}