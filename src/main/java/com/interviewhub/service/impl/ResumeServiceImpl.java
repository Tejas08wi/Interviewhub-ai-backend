package com.interviewhub.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interviewhub.entity.CandidateResume;
import com.interviewhub.entity.User;
import com.interviewhub.exception.BadRequestException;
import com.interviewhub.exception.ResourceNotFoundException;
import com.interviewhub.repository.CandidateResumeRepository;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.service.ResumeService;

import org.springframework.core.io.Resource;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final UserRepository userRepository;
    private final CandidateResumeRepository candidateResumeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ResumeServiceImpl(
            UserRepository userRepository,
            CandidateResumeRepository candidateResumeRepository) {

        this.userRepository = userRepository;
        this.candidateResumeRepository = candidateResumeRepository;
    }

    @Override
    public void uploadResume(MultipartFile file, String email) {

        validateFile(file);

        User user = getLoggedInUser(email);

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uniqueFileName = generateUniqueFileName(file);

            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            CandidateResume resume;

            if (candidateResumeRepository.existsByUser(user)) {

                resume = candidateResumeRepository
                        .findByUser(user)
                        .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

                Files.deleteIfExists(Paths.get(resume.getFilePath()));

            } else {

                resume = new CandidateResume();

                resume.setUser(user);
            }

            resume.setFileName(file.getOriginalFilename());
            resume.setFileType(file.getContentType());
            resume.setFilePath(filePath.toString());
            resume.setUploadedAt(LocalDateTime.now());

            candidateResumeRepository.save(resume);

        } catch (IOException e) {   

            throw new RuntimeException("Failed to upload resume.");
        }
    }

    // ==========================
    // Helper Methods
    // ==========================

    private User getLoggedInUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new BadRequestException("Please select a file");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new BadRequestException("Invalid file");
        }

        String lowerFileName = fileName.toLowerCase();

        if (!(lowerFileName.endsWith(".pdf")
                || lowerFileName.endsWith(".doc")
                || lowerFileName.endsWith(".docx"))) {

            throw new BadRequestException(
                    "Only PDF, DOC and DOCX files are allowed");
        }

        String contentType = file.getContentType();

        if (!(contentType.equals("application/pdf")
                || contentType.equals("application/msword")
                || contentType.equals(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

            throw new BadRequestException("Invalid file type");
        }
    }

    private String generateUniqueFileName(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();

        return UUID.randomUUID() + "_" + originalFileName;
    }

    @Override
    public Resource downloadResume(String email) {

        User user = getLoggedInUser(email);

        CandidateResume resume = candidateResumeRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Resume not found"));

        Path path = Paths.get(resume.getFilePath());

        if (!Files.exists(path)) {
            throw new ResourceNotFoundException(
                    "Resume file not found");
        }

        return new FileSystemResource(path);
    }

    @Override
    public void deleteResume(String email) {

        User user = getLoggedInUser(email);

        CandidateResume resume = candidateResumeRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        try {

            Path path = Paths.get(resume.getFilePath());

            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new RuntimeException("Failed to delete resume file.");
        }

        candidateResumeRepository.delete(resume);
    }

}