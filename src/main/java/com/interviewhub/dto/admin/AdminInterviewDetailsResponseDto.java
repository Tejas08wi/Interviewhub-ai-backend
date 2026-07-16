package com.interviewhub.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.interviewhub.enums.DifficultyLevel;

import lombok.Data;

@Data
public class AdminInterviewDetailsResponseDto {

    private Long sessionId;

    private String candidateName;

    private String candidateEmail;

    private String jobRole;

    private DifficultyLevel difficulty;

    private Integer totalQuestions;

    private Double overallScore;

    private Double technicalScore;

    private Double communicationScore;

    private String strengths;

    private String weaknesses;

    private String suggestions;

    private String finalFeedback;

    private LocalDateTime createdAt;

    private List<QuestionDetails> questions;

    @Data
    public static class QuestionDetails {

        private Integer questionNumber;

        private String category;

        private String question;

        private String expectedAnswer;

        private String candidateAnswer;

    }

}