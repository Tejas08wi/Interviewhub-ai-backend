package com.interviewhub.dto.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class CandidateProfileRequestDto {

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain exactly 10 digits")
    private String phone;

    @NotBlank(message = "Headline is required")
    private String headline;

    private String about;

    @NotBlank(message = "Experience is required")
    private String experience;

    @NotBlank(message = "Education is required")
    private String education;

    @NotBlank(message = "Skills are required")
    private String skills;

    @NotBlank(message = "Location is required")
    private String location;

    private String github;

    private String linkedin;

    private String portfolio;

    private String profilePhoto;

    public CandidateProfileRequestDto() {
    }

    // Getters and Setters
}