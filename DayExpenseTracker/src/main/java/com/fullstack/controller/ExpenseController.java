package com.fullstack.controller;

import com.fullstack.dto.*;
import com.fullstack.service.impl.ExpenseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Expenses",
        description = "APIs for managing user expenses")
@RestController
@RequestMapping("/api/ExpenseController")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Auth")
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    @Operation(summary = "Create a new expense",
            description = "Adds a new expense for the logged-in user with title, amount, date and category.")
    @PostMapping("/addExpense")
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {

        ExpenseResponse expenseResponse = expenseService.addExpense(expenseRequest);
        return new ResponseEntity<>(expenseResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Create a new List of expense",
            description = "Adds a new list of expense for the logged-in user with title, amount, date and category.")
    @PostMapping("/addExpenses")
    public ResponseEntity<List<ExpenseResponse>> addExpenses(
            @Valid @RequestBody List<ExpenseRequest> expenseRequests) {
        return new ResponseEntity<>(expenseService.addExpenses(expenseRequests), HttpStatus.CREATED);
    }

    @Operation(summary = "View all expenses (paginated)",
            description = "Returns paginated list of all expenses with sorting support. Usually for admin/report views.")
    @GetMapping("/viewAllExpenses")
    public ResponseEntity<PaginatedExpenseResponse> viewExpenses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(defaultValue = "expenseID") String sortBy) {
        return new ResponseEntity<>(expenseService.viewAllExpense(page, size, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "View expenses of a user (paginated)",
            description = "Returns paginated expenses for the given userId with sorting.")
    @GetMapping("/UserExpenses")
    public ResponseEntity<PaginatedExpenseResponse> getUserExpenses(HttpServletRequest request,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "5") int size,
                                                                    @RequestParam(defaultValue = "expenseID") String sortBy) {

        PaginatedExpenseResponse response =
                expenseService.viewAllUserExpense(page, size, sortBy);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing expense",
            description = "Updates title, amount, category and date for the selected expense.")
    @PutMapping("/updateExpense/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long expenseId, @Valid @RequestBody ExpenseUpdateRequest expenseRequest) {
        return new ResponseEntity<>(expenseService.updateExpense(expenseId, expenseRequest), HttpStatus.OK);
    }

    @Operation(summary = "Delete an expense",
            description = "Removes a single expense based on expenseID.")
    @DeleteMapping("/deleteExpense/{expenseID}")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable long expenseID) {
        expenseService.deleteExpense(expenseID);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Expense deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete all expenses",
            description = "Deletes all expense records from the system (use cautiously).")
    @DeleteMapping("/deleteAllExpense")
    public ResponseEntity<Map<String, String>> deleteAllExpense() {
        expenseService.deleteAll();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All Expense deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get total expense for selected month",
            description = "Returns total expense for a user for a given year and month")
    @GetMapping("/MonthlyTotal/total")
    public ResponseEntity<Double> getMonthlyTotal(
            HttpServletRequest request,
            @RequestParam int year,
            @RequestParam int month) {


        return new ResponseEntity<>(expenseService.getMonthlyTotal(year, month), HttpStatus.OK);
    }

    @Operation(summary = "Get category-wise spending",
            description = "Returns total amount spent per category for the given user.")
    @GetMapping("/expenses/category-summary")
    public ResponseEntity<List<CategorySpendingResponse>> getCategorySpending(
            HttpServletRequest request) {
        return new ResponseEntity<>(expenseService.getCategoryWiseSpending(), HttpStatus.OK);
    }

    @Operation(summary = "Get monthly trend report for user")
    @GetMapping("/expenses/monthly-report")
    public ResponseEntity<List<MonthlyTrendReport>> getMonthlyTrendReport(
            HttpServletRequest request,
            @RequestParam(defaultValue = "false") boolean includeCategory) {

        return new ResponseEntity<>(expenseService.getMonthlyTrendReport(includeCategory), HttpStatus.OK);
    }


}
