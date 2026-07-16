package com.interviewhub.dto.application;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequestDto {

    @NotNull(message = "Job ID is required")
    private Long jobId;

}