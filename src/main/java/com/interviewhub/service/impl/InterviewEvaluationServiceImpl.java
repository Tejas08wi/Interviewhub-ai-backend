package com.interviewhub.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewhub.dto.ai.InterviewEvaluationAiResponseDto;
import com.interviewhub.dto.interview.InterviewEvaluationResponseDto;
import com.interviewhub.entity.InterviewAnswer;
import com.interviewhub.entity.InterviewEvaluation;
import com.interviewhub.entity.InterviewQuestion;
import com.interviewhub.entity.InterviewSession;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.InterviewAnswerRepository;
import com.interviewhub.repository.InterviewEvaluationRepository;
import com.interviewhub.repository.InterviewQuestionRepository;
import com.interviewhub.repository.InterviewSessionRepository;
import com.interviewhub.service.GeminiService;
import com.interviewhub.service.InterviewEvaluationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewEvaluationServiceImpl implements InterviewEvaluationService {

    private final InterviewSessionRepository interviewSessionRepository;

    private final InterviewQuestionRepository interviewQuestionRepository;

    private final InterviewAnswerRepository interviewAnswerRepository;

    private final InterviewEvaluationRepository interviewEvaluationRepository;

    private final GeminiService geminiService;

    @Override
    public InterviewEvaluationResponseDto evaluateInterview(Long sessionId) {

        // Step 1: Fetch Interview Session
        InterviewSession interviewSession = interviewSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found."));

        // Step 2: Check if evaluation already exists
        InterviewEvaluation existingEvaluation = interviewEvaluationRepository
                .findByInterviewSession(interviewSession)
                .orElse(null);

        if (existingEvaluation != null) {
            return buildResponseDto(existingEvaluation);
        }

        // Step 3: Fetch Interview Questions
        List<InterviewQuestion> questions = interviewQuestionRepository
                .findByInterviewSession(interviewSession);

        // Step 4: Build Gemini Prompt
        String prompt = buildEvaluationPrompt(questions);

        // Step 5: Call Gemini API
        String json = geminiService.generateResponse(prompt);

        // Step 6: Parse Gemini Response
        InterviewEvaluationAiResponseDto aiResponse = parseGeminiResponse(json);

        // Step 7: Save Evaluation
        InterviewEvaluation evaluation = saveEvaluation(interviewSession, aiResponse);

        // Step 8: Return Response DTO
        return buildResponseDto(evaluation);
    }

    private String buildEvaluationPrompt(List<InterviewQuestion> questions) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                                You are an expert Java Technical Interviewer.

                                Evaluate the candidate's interview.

                                Important Rules:

                1. Return ONLY JSON.
                2. Do NOT return markdown.
                3. Do NOT return ```json.
                4. Do NOT explain anything.
                5. Scores must be integers from 0-100.
                6. Strengths must be a single string.
                7. Weaknesses must be a single string.
                8. Suggestions must be a single string.
                9. finalFeedback must be a single string.
                Return this schema only:
                                {
                                  "overallScore": 0,
                                  "technicalScore": 0,
                                  "communicationScore": 0,
                                  "strengths": "",
                                  "weaknesses": "",
                                  "suggestions": "",
                                  "finalFeedback": ""
                                }

                                Interview:

                                """);

        int questionNumber = 1;

        for (InterviewQuestion question : questions) {

            InterviewAnswer answer = interviewAnswerRepository
                    .findByInterviewQuestion(question)
                    .orElse(null);

            prompt.append("Question ")
                    .append(questionNumber++)
                    .append(":\n");

            prompt.append(question.getQuestion());

            prompt.append("\n\nExpected Answer:\n");

            prompt.append(question.getExpectedAnswer());

            prompt.append("\n\nCandidate Answer:\n");

            if (answer != null) {
                prompt.append(answer.getCandidateAnswer());
            } else {
                prompt.append("No Answer Submitted");
            }

            prompt.append("\n\n----------------------------------------\n\n");
        }

        return prompt.toString();
    }

    private InterviewEvaluationAiResponseDto parseGeminiResponse(String json) {

        try {

            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            return mapper.readValue(
                    json,
                    InterviewEvaluationAiResponseDto.class);

        } catch (Exception e) {

            throw new RuntimeException("Failed to parse Gemini response.");
        }
    }

    private InterviewEvaluation saveEvaluation(
            InterviewSession interviewSession,
            InterviewEvaluationAiResponseDto aiResponse) {

        InterviewEvaluation evaluation = InterviewEvaluation.builder()
                .interviewSession(interviewSession)
                .overallScore(aiResponse.getOverallScore())
                .technicalScore(aiResponse.getTechnicalScore())
                .communicationScore(aiResponse.getCommunicationScore())
                .strengths(aiResponse.getStrengths())
                .weaknesses(aiResponse.getWeaknesses())
                .suggestions(aiResponse.getSuggestions())
                .finalFeedback(aiResponse.getFinalFeedback())
                .createdAt(java.time.LocalDateTime.now())
                .build();

        return interviewEvaluationRepository.save(evaluation);
    }

    private InterviewEvaluationResponseDto buildResponseDto(
            InterviewEvaluation evaluation) {

        return InterviewEvaluationResponseDto.builder()
                .evaluationId(evaluation.getId())
                .sessionId(evaluation.getInterviewSession().getId())
                .overallScore(evaluation.getOverallScore())
                .technicalScore(evaluation.getTechnicalScore())
                .communicationScore(evaluation.getCommunicationScore())
                .strengths(evaluation.getStrengths())
                .weaknesses(evaluation.getWeaknesses())
                .suggestions(evaluation.getSuggestions())
                .finalFeedback(evaluation.getFinalFeedback())
                .build();
    }

    @Override
    public InterviewEvaluationResponseDto getEvaluation(Long sessionId) {

        InterviewSession interviewSession = interviewSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview session not found."));

        InterviewEvaluation evaluation = interviewEvaluationRepository
                .findByInterviewSession(interviewSession)
                .orElseThrow(() -> new ResourceNotFoundException("Interview evaluation not found."));

        return buildResponseDto(evaluation);
    }
}