package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseFilterInfo {

    @Schema(description = "Start date for filtering expenses (inclusive)", example = "2026-01-01")
    private LocalDate fromDate;

    @Schema(description = "End date for filtering expenses (inclusive)", example = "2026-01-31")
    private LocalDate toDate;

    @Schema(description = "List of category IDs to filter expenses", example = "[1, 2, 3]")
    private List<Long> categoryIds;

    @Schema(description = "Minimum amount for filtering expenses", example = "100.0")
    private Double minAmount;

    @Schema(description = "Maximum amount for filtering expenses", example = "5000.0")
    private Double maxAmount;
}
