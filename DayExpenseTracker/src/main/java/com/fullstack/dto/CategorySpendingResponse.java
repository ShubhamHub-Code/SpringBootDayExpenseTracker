package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategorySpendingResponse {

    @Schema(description = "Name of the category", example = "Electronics")
    private String categoryName;

    @Schema(description = "Total amount spent in this category", example = "2999.00")
    private Double totalAmount;

    public CategorySpendingResponse(String categoryName, Double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }
}
