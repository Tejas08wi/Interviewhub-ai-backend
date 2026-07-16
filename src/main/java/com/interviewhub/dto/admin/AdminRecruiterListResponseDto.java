package com.interviewhub.dto.admin;

import lombok.Data;

@Data
public class AdminRecruiterListResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String companyName;

    private String designation;

    private String industry;

    private String location;

}