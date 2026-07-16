package com.interviewhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interviewhub.entity.Job;
import com.interviewhub.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

        // Recruiter APIs
        List<Job> findByRecruiterProfile(RecruiterProfile recruiterProfile);

        Optional<Job> findByIdAndRecruiterProfile(
                        Long id,
                        RecruiterProfile recruiterProfile);

        // Candidate APIs
        List<Job> findByActiveTrue();

        Optional<Job> findByIdAndActiveTrue(Long id);

        @Query("""
                            SELECT j FROM Job j
                            WHERE j.active = true
                            AND (:title IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%')))
                            AND (:skills IS NULL OR LOWER(j.skills) LIKE LOWER(CONCAT('%', :skills, '%')))
                            AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
                            AND (:experience IS NULL OR LOWER(j.experience) LIKE LOWER(CONCAT('%', :experience, '%')))
                        """)
        List<Job> searchJobs(
                        @Param("title") String title,
                        @Param("skills") String skills,
                        @Param("location") String location,
                        @Param("experience") String experience);

        Page<Job> findByRecruiterProfile(
                        RecruiterProfile recruiterProfile,
                        Pageable pageable);

        Page<Job> findByActiveTrue(
                        Pageable pageable);
}