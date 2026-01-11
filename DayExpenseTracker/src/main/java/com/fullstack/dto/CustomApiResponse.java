package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomApiResponse<T> {

    @Schema(description = "Response status", example = "Success")
    private String status;

    @Schema(description = "Detailed message about the response", example = "User registered successfully")
    private String message;

    @Schema(description = "Timestamp when the response was generated", example = "2026-01-11T01:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Actual data returned by the API")
    private T data;

    @Schema(description = "List of errors if any occurred", example = "[\"Email is mandatory\", \"Password too short\"]")
    private List<String> errors;
}
