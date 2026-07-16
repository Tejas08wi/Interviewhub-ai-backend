package com.interviewhub.dto.recruiter;

import lombok.Data;

@Data
public class RecruiterProfileResponseDto {

    private Long id;

    private String companyName;

    private String designation;

    private String companyWebsite;

    private String companyDescription;

    private String industry;

    private String location;

    private String profilePhoto;

    private String recruiterName;

    private String email;
}