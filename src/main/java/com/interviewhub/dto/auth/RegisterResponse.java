package com.interviewhub.dto.auth;

import com.interviewhub.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

}