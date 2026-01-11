package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response returned after a successful login, including JWT token and user info")
public class LogInResponse {

    @Schema(description = "Unique ID of the user", example = "101")
    private long userID;

    @Schema(description = "Full name of the user", example = "Shubham Mankar")
    private String userName;

    @Schema(description = "Email address of the user", example = "shubham@example.com")
    private String userEmail;

    @Schema(description = "Message about login status", example = "Login successful")
    private String message;

    @Schema(description = "JWT token to be used for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
