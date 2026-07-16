package com.interviewhub.service;

import com.interviewhub.dto.auth.LoginRequest;
import com.interviewhub.dto.auth.LoginResponse;
import com.interviewhub.dto.auth.RegisterRequest;
import com.interviewhub.dto.auth.RegisterResponse;
import com.interviewhub.enums.Role;

public interface AuthService {

    RegisterResponse register(
            RegisterRequest request,
            Role role);

    LoginResponse login(LoginRequest request);

}