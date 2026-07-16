package com.interviewhub.dto.interview;

import java.time.LocalDateTime;

import com.interviewhub.enums.DifficultyLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewHistoryResponseDto {

    private Long sessionId;

    private String jobRole;

    private DifficultyLevel difficulty;

    private Integer totalQuestions;

    private Double overallScore;

    private LocalDateTime createdAt;

}