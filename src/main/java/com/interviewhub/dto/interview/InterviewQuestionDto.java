package com.interviewhub.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewQuestionDto {

    private Long questionId;

    private Integer questionNumber;

    private String category;

    private String question;

    private String expectedAnswer;

}