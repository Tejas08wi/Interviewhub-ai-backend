package com.interviewhub.service.impl;

import org.springframework.stereotype.Service;

import com.interviewhub.dto.interview.InterviewRequestDto;
import com.interviewhub.entity.InterviewSession;
import com.interviewhub.entity.User;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.InterviewSessionRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.InterviewSessionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewSessionServiceImpl implements InterviewSessionService {

    private final InterviewSessionRepository interviewSessionRepository;
    private final UserRepository userRepository;
    
    

    @Override
    public InterviewSession createInterviewSession(String email,
                                                   InterviewRequestDto requestDto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        InterviewSession session = new InterviewSession();

        session.setUser(user);
        session.setJobRole(requestDto.getJobRole());
        session.setDifficulty(requestDto.getDifficulty());
        session.setTotalQuestions(requestDto.getNumberOfQuestions());

        InterviewSession savedSession = interviewSessionRepository.save(session);

        return savedSession;
    }

}