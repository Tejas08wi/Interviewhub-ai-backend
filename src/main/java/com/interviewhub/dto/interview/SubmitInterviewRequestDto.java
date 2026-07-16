package com.interviewhub.dto.interview;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitInterviewRequestDto {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @Valid
    @NotEmpty(message = "At least one answer is required")
    private List<InterviewAnswerRequestDto> answers;

}