package com.interviewhub.dto.interview;

import java.util.List;

import com.interviewhub.enums.DifficultyLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponseDto {

    private Long sessionId;

    private String jobRole;

    private DifficultyLevel difficulty;

    private Integer numberOfQuestions;

    private List<InterviewQuestionDto> questions;

}