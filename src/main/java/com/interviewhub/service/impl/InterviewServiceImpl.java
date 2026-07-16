package com.interviewhub.service.impl;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewhub.dto.ai.ResumeAnalysisResponseDto;
import com.interviewhub.dto.interview.InterviewHistoryResponseDto;
import com.interviewhub.dto.interview.InterviewRequestDto;
import com.interviewhub.dto.interview.InterviewResponseDto;
import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.InterviewEvaluationRepository;
import com.interviewhub.repository.InterviewQuestionRepository;
import com.interviewhub.repository.InterviewSessionRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.GeminiService;
import com.interviewhub.service.InterviewQuestionService;
import com.interviewhub.service.InterviewService;
import com.interviewhub.service.InterviewSessionService;
import com.interviewhub.service.ResumeAnalysisService;
import com.interviewhub.service.InterviewEvaluationService;

import java.util.ArrayList;
import java.util.List;
import com.interviewhub.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

  private final ResumeAnalysisService resumeAnalysisService;
  private final GeminiService geminiService;
  private final InterviewSessionService interviewSessionService;
  private final InterviewSessionRepository interviewSessionRepository;
  private final InterviewQuestionRepository interviewQuestionRepository;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final InterviewQuestionService interviewQuestionService;
  private final InterviewEvaluationService interviewEvaluationService;
  private final InterviewEvaluationRepository interviewEvaluationRepository;

  @Override
  public InterviewResponseDto generateInterviewQuestions(
      String email,
      InterviewRequestDto requestDto) {

    ResumeAnalysisResponseDto resumeAnalysis = resumeAnalysisService.analyzeResume(email);

    String prompt = buildPrompt(resumeAnalysis, requestDto);

    String geminiResponse = geminiService.generateResponse(prompt);

    try {

      InterviewResponseDto response = objectMapper.readValue(
          geminiResponse,
          InterviewResponseDto.class);

      InterviewSession session = interviewSessionService.createInterviewSession(email, requestDto);

      response.setSessionId(session.getId());

      List<InterviewQuestion> savedQuestions = interviewQuestionService.saveInterviewQuestions(
          session,
          response.getQuestions());

      for (int i = 0; i < savedQuestions.size(); i++) {

        response.getQuestions().get(i)
            .setQuestionId(savedQuestions.get(i).getId());
      }

      return response;

    } catch (Exception e) {

      throw new RuntimeException(
          "Failed to parse Gemini response.",
          e);
    }
  }

  private String buildPrompt(ResumeAnalysisResponseDto resumeAnalysis,
      InterviewRequestDto requestDto) {

    return """
        You are an experienced technical interviewer.

        Generate %d interview questions for the following candidate.

        Job Role:
        %s

        Difficulty:
        %s

        Candidate Details:

        Skills:
        %s

        Education:
        %s

        Experience:
        %s

        Projects:
        %s

        Strengths:
        %s

        Weaknesses:
        %s

        ATS Score:
        %d

        Suggestions:
        %s

        Return ONLY valid JSON in the following format.

        {
          "jobRole": "%s",
          "difficulty": "%s",
          "numberOfQuestions": %d,
          "questions": [
            {
              "questionNumber": 1,
              "category": "Java",
              "question": "Explain JVM.",
              "expectedAnswer": "The JVM executes Java bytecode and provides platform independence."
            }
          ]
        }

        Do not return markdown.
        Do not return explanation.
        Do not wrap JSON inside ```json.
        Return only valid JSON.
        """
        .formatted(
            requestDto.getNumberOfQuestions(),
            requestDto.getJobRole(),
            requestDto.getDifficulty(),

            resumeAnalysis.getSkills(),
            resumeAnalysis.getEducation(),
            resumeAnalysis.getExperience(),
            resumeAnalysis.getProjects(),
            resumeAnalysis.getStrengths(),
            resumeAnalysis.getWeaknesses(),
            resumeAnalysis.getAtsScore(),
            resumeAnalysis.getSuggestions(),

            requestDto.getJobRole(),
            requestDto.getDifficulty(),
            requestDto.getNumberOfQuestions());
  }

  @Override
  public List<InterviewHistoryResponseDto> getInterviewHistory(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    List<InterviewSession> sessions = interviewSessionRepository.findByUser(user);

    return sessions.stream()
        .map(session -> {

          Double overallScore = interviewEvaluationRepository
              .findByInterviewSession(session)
              .map(evaluation -> evaluation.getOverallScore())
              .orElse(null);

          return new InterviewHistoryResponseDto(
              session.getId(),
              session.getJobRole(),
              session.getDifficulty(),
              session.getTotalQuestions(),
              overallScore,
              session.getCreatedAt());
        })
        .collect(Collectors.toList());
  }
}