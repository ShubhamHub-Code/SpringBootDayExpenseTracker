package com.fullstack.service;

import com.fullstack.dto.*;
import com.fullstack.entity.Users;

import java.util.List;

public interface IExpenseService {

    ExpenseResponse addExpense(ExpenseRequest expenseRequest);

    List<ExpenseResponse> addExpenses(List<ExpenseRequest> expenseRequests);

    PaginatedExpenseResponse viewAllExpense(int page, int size, String sortBy);

    public PaginatedExpenseResponse viewAllUserExpense(int page, int size, String sortBy);

    ExpenseResponse updateExpense(long expenseId, ExpenseUpdateRequest expenseRequest);

    void deleteExpense(long expenseID);

    void deleteAll();

    void deleteAllAdmin(long userId);

    Double getMonthlyTotal(int year, int month);

    List<CategorySpendingResponse> getCategoryWiseSpending();

    List<MonthlyExpenseTrend> getMonthlyTrend();

    List<MonthlyTrendReport> getMonthlyTrendReport(boolean includeCategory);

    void softDeleteExpense(Long expenseId);
}
