package com.fullstack.service;

import com.fullstack.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExpenseService {

    ExpenseResponse addExpense(ExpenseRequest expenseRequest);

    Page<ExpenseResponse> viewAllExpense(int page , int size, String sortBy);

    Page<ExpenseResponse> viewAllUserExpense(Long userId, int page, int size,String sortBy);

    ExpenseResponse updateExpense(long expenseID, ExpenseRequest expenseRequest);

    void deleteExpense(long expenseID);

    void deleteAll();

    Double getMonthlyTotal(Long userId, int year, int month);

    List<CategorySpendingResponse> getCategoryWiseSpending(Long userId);

    List<MonthlyExpenseTrend> getMonthlyTrend(Long userId);

    List<MonthlyTrendReport> getMonthlyTrendReport(Long userId, boolean includeCategory);
}
