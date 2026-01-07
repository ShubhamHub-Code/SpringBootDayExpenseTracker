package com.fullstack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Positive(message = "Amount must be greater than zero")
    private double amount;

    private String description;

    private String categoryName;
}
