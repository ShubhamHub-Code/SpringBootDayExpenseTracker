package com.fullstack.dto;

import lombok.Data;

@Data
public class MonthlyExpenseTrend {
    private int year;
    private int month;
    private Double totalAmount;

    public MonthlyExpenseTrend(int year, int month, Double totalAmount) {
        this.year = year;
        this.month = month;
        this.totalAmount = totalAmount;
    }
}
