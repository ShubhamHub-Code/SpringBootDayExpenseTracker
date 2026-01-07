package com.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedExpenseResponse {
    private int currentPage;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean hasNext;

    private boolean hasPrevious;

    private List<ExpenseResponse> expenses;

    private ExpenseFilterInfo expenseFilterInfo;
}
