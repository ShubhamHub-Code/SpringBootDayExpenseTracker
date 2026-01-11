package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseUpdateRequest {

    @Schema(description = "Title of the expense", example = "Bluetooth Headphones", required = true)
    @NotEmpty(message = "Title is required")
    private String title;

    @Schema(description = "Amount spent for this expense", example = "2999.00", required = true)
    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @Schema(description = "Category name of the expense", example = "Electronics", required = true)
    private String categoryName;

    @Schema(description = "Date of the expense in yyyy-MM-dd format", example = "2026-01-11", required = true)
    @NotNull(message = "Date is required")
    private LocalDate date;
}
