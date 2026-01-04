package com.fullstack.service.impl;

import com.fullstack.dto.*;
import com.fullstack.entity.Category;
import com.fullstack.entity.Expense;
import com.fullstack.entity.Users;
import com.fullstack.exception.InvalidRequestException;
import com.fullstack.exception.ResourceNotFoundException;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.CategoryRepository;
import com.fullstack.repository.ExpenseRepository;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.IExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements IExpenseService {

    private final ExpenseRepository expenseRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public ExpenseResponse addExpense(ExpenseRequest expenseRequest) {

        if (expenseRequest == null) {
            throw new InvalidRequestException("Request is null");
        }

        if (expenseRequest.getTitle() == null) {
            throw new InvalidRequestException("Title Not present In Request");
        }

        if (expenseRequest.getAmount() <= 0) {
            throw new InvalidRequestException("Amount is negative or zero");
        }

        Expense expense = new Expense();

        expense.setExpenseTitle(expenseRequest.getTitle());
        expense.setExpenseAmount(expenseRequest.getAmount());
        expense.setExpenseDate(LocalDate.now());
        Category category = categoryRepository.findByName(expenseRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        expense.setCategory(category);

        Users user = userRepository.findById(expenseRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        expense.setUser(user);


        Expense expense1 = expenseRepository.save(expense);

        ExpenseResponse expenseResponse = new ExpenseResponse();

        expenseResponse.setExpenseID(expense1.getExpenseID());
        expenseResponse.setTitle(expense1.getExpenseTitle());
        expenseResponse.setAmount(expense1.getExpenseAmount());
        expenseResponse.setCategoryName(category.getName());
        expenseResponse.setDate(java.sql.Date.valueOf(expense1.getExpenseDate()));

        return expenseResponse;
    }

    @Override
    public Page<ExpenseResponse> viewAllExpense(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return expenseRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Override
    public Page<ExpenseResponse> viewAllUserExpense(Long userId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return expenseRepository.findByUser_Id(userId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public ExpenseResponse updateExpense(long expenseID, ExpenseRequest expenseRequest) {

        if (expenseRequest == null) {
            throw new InvalidRequestException("Request is null");
        }

        if (expenseRequest.getTitle() == null) {
            throw new InvalidRequestException("Title Not present In Request");
        }

        if (expenseRequest.getAmount() <= 0) {
            throw new InvalidRequestException("Amount is negative or zero");
        }

        Expense expense = expenseRepository.findById(expenseID)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        expense.setExpenseTitle(expenseRequest.getTitle());
        expense.setExpenseAmount(expenseRequest.getAmount());
        expense.setExpenseDate(LocalDate.now());


        Category category = categoryRepository.findByName(expenseRequest.getCategoryName()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        expense.setCategory(category);

        Users user = userRepository.findById(expenseRequest.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);


        ExpenseResponse response = new ExpenseResponse();
        response.setExpenseID(saved.getExpenseID());
        response.setTitle(saved.getExpenseTitle());
        response.setAmount(saved.getExpenseAmount());
        response.setCategoryName(category.getName());
        response.setDate(java.sql.Date.valueOf(saved.getExpenseDate()));

        return response;

    }

    @Override
    public void deleteExpense(long expenseID) {
        expenseRepository.deleteById(expenseID);
    }

    @Override
    public void deleteAll() {
        expenseRepository.deleteAll();
    }

    public Double getMonthlyTotal(Long userId, int year, int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return expenseRepository.getTotalExpenseForMonth(userId, start, end);
    }

    public List<CategorySpendingResponse> getCategoryWiseSpending(Long userId) {
        return expenseRepository.getCategoryWiseSpending(userId);
    }


    public List<MonthlyExpenseTrend> getMonthlyTrend(Long userId) {
        return expenseRepository.getMonthlyTrend(userId);
    }

    public List<MonthlyTrendReport> getMonthlyTrendReport(Long userId, boolean includeCategory) {

        List<MonthlyExpenseTrend> monthlyTrends = expenseRepository.getMonthlyTrend(userId);
        List<MonthlyTrendReport> reports = new ArrayList<>();

        for (MonthlyExpenseTrend trend : monthlyTrends) {
            List<CategorySpendingResponse> categorySpending = null;

            if (includeCategory) {
                categorySpending = expenseRepository.getCategorySpendingForMonth(
                        userId, trend.getYear(), trend.getMonth()
                );
            }

            reports.add(new MonthlyTrendReport(
                    trend.getYear(),
                    trend.getMonth(),
                    trend.getTotalAmount(),
                    categorySpending
            ));
        }

        return reports;
    }


    private ExpenseResponse convertToResponse(Expense expense) {

        ExpenseResponse res = new ExpenseResponse();

        res.setExpenseID(expense.getExpenseID());
        res.setTitle(expense.getExpenseTitle());
        res.setAmount(expense.getExpenseAmount());
        res.setCategoryName(
                expense.getCategory() != null ? expense.getCategory().getName() : null
        );
        res.setDate(java.sql.Date.valueOf(expense.getExpenseDate()));

        return res;
    }


}
