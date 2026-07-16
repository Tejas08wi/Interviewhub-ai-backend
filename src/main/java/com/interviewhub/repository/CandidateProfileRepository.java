package com.interviewhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewhub.entity.CandidateProfile;
import com.interviewhub.entity.User;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser(User user);

    boolean existsByUser(User user);
}