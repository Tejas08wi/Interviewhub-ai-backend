package com.interviewhub.dto.application;

import com.interviewhub.enums.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationStatusUpdateRequestDto {

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;

}