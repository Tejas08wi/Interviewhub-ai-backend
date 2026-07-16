package com.interviewhub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewhub.dto.response.ApiResponse;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public ResponseEntity<ApiResponse<String>> testApi() {

        ApiResponse<String> response = new ApiResponse<>(
                true,
                "InterviewHub AI Backend is Running Successfully!",
                "Welcome to InterviewHub AI");

        return ResponseEntity.ok(response);
    }

}