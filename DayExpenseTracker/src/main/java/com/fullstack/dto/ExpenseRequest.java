package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseRequest {

    @Schema(description = "Title of the expense", example = "Bluetooth Headphones", required = true)
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Amount spent for the expense", example = "2999.00", required = true)
    @Positive(message = "Amount must be greater than zero")
    private double amount;

    @Schema(description = "Optional description for the expense", example = "Wireless noise-cancelling headphones")
    private String description;

    @Schema(description = "Category name under which this expense falls", example = "Electronics", required = true)
    @NotBlank(message = "Category name is required")
    private String categoryName;
}
