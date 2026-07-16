package com.interviewhub.dto.admin;

import lombok.Data;

@Data
public class AdminCandidateListResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String headline;

    private String location;

}