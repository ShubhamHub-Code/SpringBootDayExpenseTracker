package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload for creating or updating a user")
public class UserRequest {

    @Schema(description = "Full name of the user", example = "Shubham Mankar", required = true)
    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @Schema(description = "Email address of the user", example = "shubham@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @Schema(description = "Password for the user account (min 5 characters, at least 1 special character)",
            example = "P@ssw0rd!", required = true)
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>]).{5,}$",
            message = "Password must be at least 5 characters long and contain at least one special character")
    private String password;
}
