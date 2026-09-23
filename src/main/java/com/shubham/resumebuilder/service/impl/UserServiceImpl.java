package com.shubham.resumebuilder.service.impl;

import com.shubham.resumebuilder.dto.AuthResponse;
import com.shubham.resumebuilder.dto.RegisterRequest;
import com.shubham.resumebuilder.entity.User;
import com.shubham.resumebuilder.exceptions.ResourceExistsException;
import com.shubham.resumebuilder.repository.UserRepository;
import com.shubham.resumebuilder.service.EmailService;
import com.shubham.resumebuilder.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    private final UserRepository userRepository;
    private final EmailService emailService;


    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User with this email Already Exists :" +request.getEmail());
        }

        User newUser = toDocument(request);

        User savedUser = userRepository.save(newUser);

        sendVerificationEmail(savedUser);

        return toResponse(savedUser);
    }

    @Override
    public void sendVerificationEmail(User savedUser) {
        try {
            String link = appBaseUrl + "/api/auth/verify-email?token=" + savedUser.getVerificationToken();
            String html = "<div style='font-family:sans-serif'>" +

                    "<h2>Verify your email</h2>" +

                    "<p>Hi " + savedUser.getName() + ", please confirm your email to activate your account.</p>" +

                    "<p><a href='" + link + "' " +
                    "style='display:inline-block;padding:10px 16px;background:#6366f1;" +
                    "color:white;text-decoration:none;border-radius:6px;'>" +
                    "Verify Email</a></p>" +

                    "<p>Or copy this link: " + link + "</p>" +

                    "<p>This link expires in 24 hours.</p>" +

                    "</div>";
            emailService.sendHtmlEmail(savedUser.getEmail(), "Verify your Email", html);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to send Verification email: " + e.getMessage());
        }
    }


    private User toDocument(RegisterRequest request){
         return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    private AuthResponse toResponse(User savedUser){
        return AuthResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .subscriptionPlan(savedUser.getSubscriptionPlan())
                .emailVerified(savedUser.isEmailVerified())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }


}
