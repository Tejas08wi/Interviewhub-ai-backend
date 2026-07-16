package com.interviewhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewhub.entity.CandidateResume;
import com.interviewhub.entity.User;

public interface CandidateResumeRepository
        extends JpaRepository<CandidateResume, Long> {

    Optional<CandidateResume> findByUser(User user);

    boolean existsByUser(User user);

    void deleteByUser(User user);

}