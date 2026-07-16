package com.interviewhub.controller.interview;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.dto.interview.InterviewSubmissionResponseDto;
import com.interviewhub.dto.interview.InterviewEvaluationResponseDto;
import com.interviewhub.dto.interview.InterviewHistoryResponseDto;
import com.interviewhub.dto.interview.InterviewRequestDto;
import com.interviewhub.dto.interview.InterviewResponseDto;
import com.interviewhub.dto.interview.SubmitInterviewRequestDto;
import com.interviewhub.service.InterviewAnswerService;
import com.interviewhub.service.InterviewEvaluationService;
import com.interviewhub.service.InterviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
@Validated
public class InterviewController {

        private final InterviewService interviewService;
        private final InterviewAnswerService interviewAnswerService;
        private final InterviewEvaluationService interviewEvaluationService;

        @PostMapping("/generate")
        public ResponseEntity<ApiResponse<InterviewResponseDto>> generateInterviewQuestions(
                        Authentication authentication,
                        @Valid @RequestBody InterviewRequestDto requestDto) {

                String email = authentication.getName();

                InterviewResponseDto response = interviewService.generateInterviewQuestions(email, requestDto);

                ApiResponse<InterviewResponseDto> apiResponse = new ApiResponse<>(
                                true,
                                "Interview questions generated successfully.",
                                response);

                return ResponseEntity.ok(apiResponse);
        }

        @GetMapping("/history")
        public ResponseEntity<ApiResponse<List<InterviewHistoryResponseDto>>> getInterviewHistory(
                        Authentication authentication) {

                String email = authentication.getName();

                List<InterviewHistoryResponseDto> history = interviewService.getInterviewHistory(email);

                ApiResponse<List<InterviewHistoryResponseDto>> apiResponse = new ApiResponse<>(
                                true,
                                "Interview history fetched successfully.",
                                history);

                return ResponseEntity.ok(apiResponse);
        }

        @PostMapping("/submit")
        public ResponseEntity<ApiResponse<InterviewSubmissionResponseDto>> submitInterview(

                        Authentication authentication,

                        @Valid @RequestBody SubmitInterviewRequestDto requestDto) {

                String email = authentication.getName();

                InterviewSubmissionResponseDto response = interviewAnswerService.submitInterview(email, requestDto);

                ApiResponse<InterviewSubmissionResponseDto> apiResponse = new ApiResponse<>(
                                true,
                                "Interview submitted successfully.",
                                response);

                return ResponseEntity.ok(apiResponse);
        }

        @GetMapping("/evaluation/{sessionId}")
        public ResponseEntity<ApiResponse<InterviewEvaluationResponseDto>> getEvaluation(
                        @PathVariable Long sessionId) {

                InterviewEvaluationResponseDto response = interviewEvaluationService.getEvaluation(sessionId);

                ApiResponse<InterviewEvaluationResponseDto> apiResponse = new ApiResponse<>(
                                true,
                                "Interview evaluation fetched successfully.",
                                response);

                return ResponseEntity.ok(apiResponse);
        }

}