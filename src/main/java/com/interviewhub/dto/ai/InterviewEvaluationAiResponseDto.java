package com.interviewhub.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEvaluationAiResponseDto {

    private Double overallScore;

    private Double technicalScore;

    private Double communicationScore;

    private String strengths;

    private String weaknesses;

    private String suggestions;

    private String finalFeedback;

}