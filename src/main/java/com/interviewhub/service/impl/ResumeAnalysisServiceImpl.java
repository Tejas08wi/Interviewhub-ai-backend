package com.interviewhub.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewhub.dto.ai.ResumeAnalysisResponseDto;
import com.interviewhub.entity.CandidateResume;
import com.interviewhub.entity.User;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.CandidateResumeRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.GeminiService;
import com.interviewhub.service.ResumeAnalysisService;

@Service
public class ResumeAnalysisServiceImpl
        implements ResumeAnalysisService {

    private final UserRepository userRepository;
    private final CandidateResumeRepository candidateResumeRepository;
    private final GeminiService geminiService;

    public ResumeAnalysisServiceImpl(
            UserRepository userRepository,
            CandidateResumeRepository candidateResumeRepository,
            GeminiService geminiService) {

        this.userRepository = userRepository;
        this.candidateResumeRepository = candidateResumeRepository;
        this.geminiService = geminiService;
    }

    @Override
    public ResumeAnalysisResponseDto analyzeResume(String email) {

        User user = getLoggedInUser(email);

        CandidateResume resume = candidateResumeRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resume not found"));

        File pdfFile = new File(resume.getFilePath());

        if (!pdfFile.exists()) {
            throw new ResourceNotFoundException(
                    "Resume file not found");
        }

        try (PDDocument document = Loader.loadPDF(pdfFile)) {

            PDFTextStripper stripper = new PDFTextStripper();

            String resumeText = stripper.getText(document);

            String prompt = buildPrompt(resumeText);

            String json = geminiService.generateResponse(prompt);

            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

            return mapper.readValue(
                    json,
                    ResumeAnalysisResponseDto.class);

        } catch (Exception e) {

            e.printStackTrace(); // Temporary for debugging

            throw new RuntimeException(e.getMessage());
        }
    }

    // ===========================
    // Helper Methods
    // ===========================

    private User getLoggedInUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"));
    }

    private String buildPrompt(String resumeText) {

        return """
                You are an ATS Resume Analyzer.

                Analyze the following resume.

                Return ONLY valid JSON.

                Rules:

                1. education must be a single string.
                2. experience must be a single string.
                3. skills must always be an array.
                4. projects must always be an array.
                5. strengths must always be an array.
                6. weaknesses must always be an array.
                7. suggestions must always be an array.
                8. atsScore must always be an integer.
                9. Do not return markdown.
                10. Do not use ```json.
                11. Do not add explanations.

                Schema:

                {
                  "skills": [],
                  "education": "",
                  "experience": "",
                  "projects": [],
                  "strengths": [],
                  "weaknesses": [],
                  "atsScore": 0,
                  "suggestions": []
                }

                Resume:

                """ + resumeText;
    }
}