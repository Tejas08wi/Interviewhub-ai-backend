package com.interviewhub.dto.candidate;

import lombok.Data;

@Data
public class CandidateProfileResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String headline;

    private String about;

    private String experience;

    private String education;

    private String skills;

    private String location;

    private String github;

    private String linkedin;

    private String portfolio;

    private String profilePhoto;
}