package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Monthly trend report showing total amount spent per month and optional category-wise breakdown")
public class MonthlyTrendReport {

    @Schema(description = "Year of the report", example = "2026")
    private int year;

    @Schema(description = "Month of the report (1-12)", example = "1")
    private int month;

    @Schema(description = "Total amount spent in the month", example = "12999.50")
    private Double totalAmount;

    @Schema(description = "Optional category-wise spending breakdown for the month")
    private List<CategorySpendingResponse> categorySpending;

    public MonthlyTrendReport(int year, int month, Double totalAmount, List<CategorySpendingResponse> categorySpending) {
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
        this.categorySpending = categorySpending;
    }
}
