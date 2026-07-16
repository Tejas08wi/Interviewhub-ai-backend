package com.interviewhub.dto.interview;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InterviewAnswerRequestDto {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Candidate answer is required")
    private String candidateAnswer;

}