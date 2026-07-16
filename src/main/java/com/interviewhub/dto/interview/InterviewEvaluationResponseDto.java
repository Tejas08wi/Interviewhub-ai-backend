package com.interviewhub.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEvaluationResponseDto {

    private Long evaluationId;

    private Long sessionId;

    private Double overallScore;

    private Double technicalScore;

    private Double communicationScore;

    private String strengths;

    private String weaknesses;

    private String suggestions;

    private String finalFeedback;

}