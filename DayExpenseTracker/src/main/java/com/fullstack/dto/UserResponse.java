package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response payload returned after creating, updating, or fetching a user")
public class UserResponse {

    @Schema(description = "Unique ID of the user", example = "101")
    private Long id;

    @Schema(description = "Full name of the user", example = "Shubham Mankar")
    private String name;

    @Schema(description = "Email address of the user", example = "shubham@example.com")
    private String email;

    @Schema(description = "Message about the operation", example = "User created successfully")
    private String message;
}
