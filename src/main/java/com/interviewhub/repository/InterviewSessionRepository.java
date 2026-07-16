package com.interviewhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interviewhub.entity.InterviewSession;
import com.interviewhub.entity.User;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    List<InterviewSession> findByUser(User user);

    void deleteByUser(User user);

}