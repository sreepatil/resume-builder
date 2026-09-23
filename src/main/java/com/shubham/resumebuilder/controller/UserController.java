package com.shubham.resumebuilder.controller;

import com.shubham.resumebuilder.dto.AuthResponse;
import com.shubham.resumebuilder.dto.RegisterRequest;
import com.shubham.resumebuilder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        log.info("Inside AuthController: register() {}", request);
        AuthResponse response = userService.register(request);
        log.info("Inside AuthController: register() {}", response);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        log.info("Inside AuthController: verifyEmail() {}", token);
        userService.verifyEmail(token);

        return ResponseEntity.status(200).body(Map.of("message", "Email verified Successfully"));
    }
}
