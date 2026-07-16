package com.interviewhub.controller.resume;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.interviewhub.dto.response.ApiResponse;
import com.interviewhub.service.ResumeService;

import org.springframework.http.MediaType;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> uploadResume(

            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        String email = authentication.getName();

        resumeService.uploadResume(file, email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Resume uploaded successfully",
                null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResume(
            Authentication authentication) throws IOException {

        String email = authentication.getName();

        Resource resource = resumeService.downloadResume(email);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> deleteResume(
            Authentication authentication) {

        String email = authentication.getName();

        resumeService.deleteResume(email);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Resume deleted successfully",
                null);

        return ResponseEntity.ok(response);
    }
}