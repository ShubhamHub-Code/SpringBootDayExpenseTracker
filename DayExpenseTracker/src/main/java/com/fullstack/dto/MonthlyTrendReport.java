package com.fullstack.dto;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyTrendReport{

    private int year;
    private int month;
    private Double totalAmount;
    private List<CategorySpendingResponse> categorySpending; // optional

    public MonthlyTrendReport(int year, int month, Double totalAmount, List<CategorySpendingResponse> categorySpending) {
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
        this.categorySpending = categorySpending;
    }

}
