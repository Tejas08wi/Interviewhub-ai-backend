package com.interviewhub.dto.recruiter;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecruiterProfileRequestDto {

    @NotBlank(message = "Company name is required.")
    private String companyName;

    @NotBlank(message = "Designation is required.")
    private String designation;

    private String companyWebsite;

    private String companyDescription;

    private String industry;

    private String location;

    private String profilePhoto;
}