package com.interviewhub.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSubmissionResponseDto {

    private Long sessionId;

    private Integer totalAnswersSubmitted;

    private String message;

    private InterviewEvaluationResponseDto evaluation;
}