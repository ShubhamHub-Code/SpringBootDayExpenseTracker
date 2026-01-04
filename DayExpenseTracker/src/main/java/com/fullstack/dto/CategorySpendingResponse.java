package com.fullstack.dto;

import lombok.Data;

@Data
public class CategorySpendingResponse {

    private String categoryName;
    private Double totalAmount;

    public CategorySpendingResponse(String categoryName, Double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }
}
