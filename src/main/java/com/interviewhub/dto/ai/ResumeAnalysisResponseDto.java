package com.interviewhub.dto.ai;

import java.util.List;

import lombok.Data;

@Data
public class ResumeAnalysisResponseDto {

    private List<String> skills;

    private String education;

    private String experience;

    private List<String> projects;

    private List<String> strengths;

    private List<String> weaknesses;

    private Integer atsScore;

    private List<String> suggestions;

}