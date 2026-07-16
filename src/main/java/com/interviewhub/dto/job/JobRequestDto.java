package com.interviewhub.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class JobRequestDto {

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    @NotBlank(message = "Skills are required")
    private String skills;

    @NotBlank(message = "Experience is required")
    private String experience;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @PositiveOrZero(message = "Minimum salary cannot be negative")
    private Double minSalary;

    @PositiveOrZero(message = "Maximum salary cannot be negative")
    private Double maxSalary;

    private Boolean active;
}