package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login request containing user credentials")
public record LogInRequest(
        @Schema(description = "Email address of the user", example = "shubham@example.com", required = true)
        String email,

        @Schema(description = "Password for the user account", example = "P@ssw0rd", required = true)
        String password
) {}
