package com.fullstack.service.impl;

import com.fullstack.dto.*;
import com.fullstack.entity.Category;
import com.fullstack.entity.Expense;
import com.fullstack.entity.Users;
import com.fullstack.exception.InvalidRequestException;
import com.fullstack.exception.ResourceNotFoundException;
import com.fullstack.exception.UnauthorizedActionException;
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

    private final CategoryServiceImpl categoryService;

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

        Long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenseID(saved.getExpenseID());
        expenseResponse.setTitle(saved.getExpenseTitle());
        expenseResponse.setAmount(saved.getExpenseAmount());
        expenseResponse.setCategoryName(category.getName());
        expenseResponse.setDate(java.sql.Date.valueOf(saved.getExpenseDate()));

        return expenseResponse;
    }

    @Transactional
    @Override
    public List<ExpenseResponse> addExpenses( List<ExpenseRequest> expenseRequests) {

        if (expenseRequests == null || expenseRequests.isEmpty()) {
            throw new InvalidRequestException("Request list is empty or null");
        }

        List<Expense> expensesToSave = new ArrayList<>();
        List<ExpenseResponse> responses = new ArrayList<>();


        for (ExpenseRequest request : expenseRequests) {

            if (request == null) {
                throw new InvalidRequestException("Request is null");
            }
            if (request.getTitle() == null) {
                throw new InvalidRequestException("Title not present in request");
            }
            if (request.getAmount() <= 0) {
                throw new InvalidRequestException("Amount is negative or zero");
            }

            Expense expense = new Expense();
            expense.setExpenseTitle(request.getTitle());
            expense.setExpenseAmount(request.getAmount());
            expense.setExpenseDate(LocalDate.now());

            Category category = categoryRepository.findByName(request.getCategoryName())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            expense.setCategory(category);

            Long userId = categoryService.getLoggedInUserId();
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            expense.setUser(user);

            expensesToSave.add(expense);
        }

        List<Expense> savedExpenses = expenseRepository.saveAll(expensesToSave);

        for (Expense expense : savedExpenses) {
            ExpenseResponse response = new ExpenseResponse();
            response.setExpenseID(expense.getExpenseID());
            response.setTitle(expense.getExpenseTitle());
            response.setAmount(expense.getExpenseAmount());
            response.setCategoryName(expense.getCategory().getName());
            response.setDate(java.sql.Date.valueOf(expense.getExpenseDate()));
            responses.add(response);
        }

        return responses;
    }


    @Override
    public PaginatedExpenseResponse viewAllExpense(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<ExpenseResponse> expensePage =
                expenseRepository.findAll(pageable)
                        .map(this::convertToResponse);

        PaginatedExpenseResponse response = new PaginatedExpenseResponse();
        response.setCurrentPage(expensePage.getNumber() + 1);
        response.setPageSize(expensePage.getSize());
        response.setTotalElements(expensePage.getTotalElements());
        response.setTotalPages(expensePage.getTotalPages());
        response.setExpenses(expensePage.getContent());

        response.setHasNext(expensePage.hasNext());
        response.setHasPrevious(expensePage.hasPrevious());

        return response;
    }

    @Override
    public PaginatedExpenseResponse viewAllUserExpense(int page, int size, String sortBy) {

        Long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<ExpenseResponse> expensePage =
                expenseRepository.findByUser_Id(user.getId(), pageable)
                        .map(this::convertToResponse);

        PaginatedExpenseResponse response = new PaginatedExpenseResponse();
        response.setCurrentPage(expensePage.getNumber() + 1);
        response.setPageSize(expensePage.getSize());
        response.setTotalElements(expensePage.getTotalElements());
        response.setTotalPages(expensePage.getTotalPages());
        response.setExpenses(expensePage.getContent());

        response.setHasNext(expensePage.hasNext());
        response.setHasPrevious(expensePage.hasPrevious());

        return response;
    }

    @Override
    public ExpenseResponse updateExpense(long expenseId, ExpenseUpdateRequest expenseRequest) {

        if (expenseRequest == null) {
            throw new InvalidRequestException("Request is null");
        }

        long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You cannot update another user's expense");
        }

        expense.setExpenseTitle(expenseRequest.getTitle());
        expense.setExpenseAmount(expenseRequest.getAmount());
        expense.setExpenseDate(expenseRequest.getDate());


        Category category = categoryRepository.findByName(expenseRequest.getCategoryName()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        expense.setCategory(category);

        Expense saved = expenseRepository.save(expense);
        ExpenseResponse response = new ExpenseResponse();
        response.setExpenseID(saved.getExpenseID());
        response.setTitle(saved.getExpenseTitle());
        response.setAmount(saved.getExpenseAmount());
        response.setCategoryName(category.getName());
        response.setDate(java.sql.Date.valueOf(saved.getExpenseDate()));
        response.setMassage("Expense updated successfully");

        return response;

    }

    @Override
    public void deleteExpense(long expenseID) {

        long userId = categoryService.getLoggedInUserId();
        Expense expense = expenseRepository.findById(expenseID).orElseThrow(()->new ResourceNotFoundException("Category Not Found"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("You cannot delete this expense");
        }else{
            expenseRepository.deleteById(expenseID);
        }
    }

    @Override
    public void deleteAll() {
        long userId = categoryService.getLoggedInUserId();
        expenseRepository.deleteById(userId);
    }

    @Override
    public void deleteAllAdmin(long userId) {
        expenseRepository.deleteById(userId);
    }

    @Override
    public Double getMonthlyTotal(int year, int month) {

        long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return expenseRepository.getTotalExpenseForMonth(user.getId(), start, end);
    }

    @Override
    public List<CategorySpendingResponse> getCategoryWiseSpending() {

        long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return expenseRepository.getCategoryWiseSpending(user.getId());
    }


    public List<MonthlyExpenseTrend> getMonthlyTrend() {
        long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return expenseRepository.getMonthlyTrend(userId);
    }

    @Override
    public List<MonthlyTrendReport> getMonthlyTrendReport(boolean includeCategory) {

        long userId = categoryService.getLoggedInUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<MonthlyExpenseTrend> monthlyTrends = expenseRepository.getMonthlyTrend(user.getId());
        List<MonthlyTrendReport> reports = new ArrayList<>();

        for (MonthlyExpenseTrend trend : monthlyTrends) {
            List<CategorySpendingResponse> categorySpending = null;

            if (includeCategory) {
                categorySpending = expenseRepository.getCategorySpendingForMonth(
                        user.getId(), trend.getYear(), trend.getMonth()
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
