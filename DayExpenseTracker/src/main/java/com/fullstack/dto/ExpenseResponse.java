package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    @Schema(description = "Unique ID of the expense", example = "501")
    private long expenseID;

    @Schema(description = "Title of the expense", example = "Bluetooth Headphones")
    private String title;

    @Schema(description = "Amount spent for this expense", example = "2999.00")
    private double amount;

    @Schema(description = "Category name of the expense", example = "Electronics")
    private String categoryName;

    @Schema(description = "Date when the expense occurred", example = "2026-01-11T00:00:00Z")
    private Date date;
}
