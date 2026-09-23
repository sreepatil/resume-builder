package com.shubham.resumebuilder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    @JsonProperty("_id")
    private String id;
    private String name;
    private String email;
    private String password;
    private String profileImageUrl;
    private String subscriptionPlan;
    private boolean emailVerified;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
