package com.interviewhub.dto.job;

import lombok.Data;

@Data
public class JobResponseDto {

    private Long id;

    private String jobTitle;

    private String jobDescription;

    private String skills;

    private String experience;

    private String location;

    private String employmentType;

    private Double minSalary;

    private Double maxSalary;

    private Boolean active;

    private String companyName;

    private String recruiterName;
}