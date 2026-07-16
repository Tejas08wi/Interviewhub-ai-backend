package com.interviewhub.dto.interview;

import com.interviewhub.enums.DifficultyLevel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewRequestDto {

    @NotBlank(message = "Job role is required")
    private String jobRole;

    @NotNull(message = "Difficulty is required")
    private DifficultyLevel difficulty;

    @NotNull(message = "Number of questions is required")
    @Min(value = 1, message = "Minimum 1 question is required")
    @Max(value = 20, message = "Maximum 20 questions are allowed")
    private Integer numberOfQuestions;
}