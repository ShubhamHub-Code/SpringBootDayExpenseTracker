package com.fullstack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Paginated response for user expenses, including paging info and applied filters")
public class PaginatedExpenseResponse {

    @Schema(description = "Current page number (zero-based index)", example = "0")
    private int currentPage;

    @Schema(description = "Number of items per page", example = "5")
    private int pageSize;

    @Schema(description = "Total number of expense elements available", example = "42")
    private long totalElements;

    @Schema(description = "Total number of pages available", example = "9")
    private int totalPages;

    @Schema(description = "Whether there is a next page available", example = "true")
    private boolean hasNext;

    @Schema(description = "Whether there is a previous page available", example = "false")
    private boolean hasPrevious;

    @Schema(description = "List of expenses on the current page")
    private List<ExpenseResponse> expenses;

    @Schema(description = "Filters applied to the current expense query")
    private ExpenseFilterInfo expenseFilterInfo;
}
