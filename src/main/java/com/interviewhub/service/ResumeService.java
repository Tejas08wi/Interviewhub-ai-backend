package com.interviewhub.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface ResumeService {

    void uploadResume(
            MultipartFile file,
            String email);

    Resource downloadResume(
            String email);

    void deleteResume(String email);

}