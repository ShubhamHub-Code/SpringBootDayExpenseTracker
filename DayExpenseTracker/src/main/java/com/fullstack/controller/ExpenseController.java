package com.fullstack.controller;

import com.fullstack.dto.*;
import com.fullstack.service.impl.ExpenseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Expenses", description = "APIs for managing user expenses")
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    @Operation(summary = "Create a new expense",
            description = "Adds a new expense for the logged-in user with title, amount, date, and category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid expense data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse<ExpenseResponse>> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        log.info("Received request to add new expense: {}", expenseRequest.getCategoryName());
        ExpenseResponse expenseResponse = expenseService.addExpense(expenseRequest);
        CustomApiResponse<ExpenseResponse> customApiResponse = new CustomApiResponse<>(
                "Success", "New Expense Added Successfully", LocalDateTime.now(), expenseResponse, null
        );
        return new ResponseEntity<>(customApiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple expenses",
            description = "Adds a list of expenses for the logged-in user with title, amount, date, and category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expenses created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid expense data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/bulk")
    public ResponseEntity<CustomApiResponse<List<ExpenseResponse>>> addExpenses(@Valid @RequestBody List<ExpenseRequest> expenseRequests) {
        log.info("Received request to add list of expenses");
        List<ExpenseResponse> responses = expenseService.addExpenses(expenseRequests);
        CustomApiResponse<List<ExpenseResponse>> customApiResponse = new CustomApiResponse<>(
                "Success", "New List of Expenses Added Successfully", LocalDateTime.now(), responses, null
        );
        return new ResponseEntity<>(customApiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "View user expenses (paginated)",
            description = "Returns paginated expenses for the logged-in user with sorting options.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    public ResponseEntity<CustomApiResponse<PaginatedExpenseResponse>> getUserExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "expenseID") String sortBy
    ) {
        log.info("Received request to fetch user expenses");
        PaginatedExpenseResponse response = expenseService.viewAllUserExpense(page, size, sortBy);
        CustomApiResponse<PaginatedExpenseResponse> customApiResponse = new CustomApiResponse<>(
                "Success", "User Expenses fetched successfully", LocalDateTime.now(), response, null
        );
        return new ResponseEntity<>(customApiResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get total expense for a specific month",
            description = "Returns total expense for the logged-in user for a given year and month.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly total fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/monthly-total")
    public ResponseEntity<CustomApiResponse<Double>> getMonthlyTotal(@RequestParam int year, @RequestParam int month) {
        Double total = expenseService.getMonthlyTotal(year, month);
        CustomApiResponse<Double> response = new CustomApiResponse<>(
                "Success", "Monthly total expense fetched successfully", LocalDateTime.now(), total, null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get category-wise spending",
            description = "Returns total amount spent per category for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category-wise spending fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/category-summary")
    public ResponseEntity<CustomApiResponse<List<CategorySpendingResponse>>> getCategorySpending() {
        List<CategorySpendingResponse> summary = expenseService.getCategoryWiseSpending();
        CustomApiResponse<List<CategorySpendingResponse>> response = new CustomApiResponse<>(
                "Success", "Category-wise spending fetched successfully", LocalDateTime.now(), summary, null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get monthly trend report",
            description = "Returns month-wise trend report for the user, optionally including categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly trend report fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/monthly-report")
    public ResponseEntity<CustomApiResponse<List<MonthlyTrendReport>>> getMonthlyTrendReport(
            @RequestParam(defaultValue = "false") boolean includeCategory
    ) {
        List<MonthlyTrendReport> report = expenseService.getMonthlyTrendReport(includeCategory);
        CustomApiResponse<List<MonthlyTrendReport>> response = new CustomApiResponse<>(
                "Success", "Monthly trend report fetched successfully", LocalDateTime.now(), report, null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Soft delete an expense",
            description = "Marks an expense as deleted without removing it from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        expenseService.softDeleteExpense(id);
        CustomApiResponse<Void> response = new CustomApiResponse<>(
                "Success", "Expense deleted successfully", LocalDateTime.now(), null, null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
