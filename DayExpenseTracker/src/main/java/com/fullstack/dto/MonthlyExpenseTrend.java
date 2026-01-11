package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Monthly expense trend showing total amount spent per month")
public class MonthlyExpenseTrend {

    @Schema(description = "Year of the expense", example = "2026")
    private int year;

    @Schema(description = "Month of the expense (1-12)", example = "1")
    private int month;

    @Schema(description = "Total amount spent in the month", example = "12999.50")
    private Double totalAmount;

    public MonthlyExpenseTrend(int year, int month, Double totalAmount) {
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
    }
}
