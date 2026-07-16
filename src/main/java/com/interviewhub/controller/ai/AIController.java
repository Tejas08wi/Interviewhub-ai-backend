package com.interviewhub.controller.ai;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.GeminiService;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<String>> testGemini() {

        String response = geminiService.generateResponse(
                "Hello Gemini! Who are you? Reply in 3 lines.");

        ApiResponse<String> apiResponse =
                new ApiResponse<>(
                        true,
                        "Gemini API connected successfully",
                        response);

        return ResponseEntity.ok(apiResponse);
    }

}