package com.shubham.resumebuilder.service;

import com.shubham.resumebuilder.dto.AuthResponse;
import com.shubham.resumebuilder.dto.RegisterRequest;
import com.shubham.resumebuilder.entity.User;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    void sendVerificationEmail(User savedUser);
}
