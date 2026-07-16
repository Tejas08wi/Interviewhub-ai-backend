package com.interviewhub.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.interviewhub.dto.auth.LoginRequest;
import com.interviewhub.dto.auth.LoginResponse;
import com.interviewhub.dto.auth.RegisterRequest;
import com.interviewhub.dto.auth.RegisterResponse;
import com.interviewhub.entity.User;
import com.interviewhub.enums.Role;
import com.interviewhub.exception.InvalidCredentialsException;
import com.interviewhub.exception.UserAlreadyExistsException;
import com.interviewhub.repository.UserRepository;
import com.interviewhub.security.JwtService;
import com.interviewhub.service.AuthService;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}

    @Override
    public RegisterResponse register(RegisterRequest request,Role role) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered.");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(role);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole());
    }

    @Override
public LoginResponse login(LoginRequest request) {

    User user = userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new InvalidCredentialsException("Invalid email or password."));

    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        throw new InvalidCredentialsException("Invalid email or password.");
    }

    String token = jwtService.generateToken(
            user.getEmail(),
            user.getRole().name());

    return new LoginResponse(token);

}

}