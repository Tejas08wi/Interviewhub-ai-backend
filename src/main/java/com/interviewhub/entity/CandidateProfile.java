package com.interviewhub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "candidate_profiles")
@Data
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String headline;

    @Column(length = 2000)
    private String about;

    private String experience;

    private String education;

    private String skills;

    private String location;

    private String github;

    private String linkedin;

    private String portfolio;

    private String profilePhoto;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public CandidateProfile() {
    }

   
}