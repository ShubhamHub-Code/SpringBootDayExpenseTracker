package com.fullstack.controller;

import com.fullstack.dto.CategorySpendingResponse;
import com.fullstack.dto.ExpenseRequest;
import com.fullstack.dto.ExpenseResponse;
import com.fullstack.dto.MonthlyTrendReport;
import com.fullstack.service.impl.ExpenseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Expenses",
        description = "APIs for managing user expenses"
)
@RestController
@RequestMapping("/api/ExpenseController")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    @Operation(
            summary = "Create a new expense",
            description = "Adds a new expense for the logged-in user with title, amount, date and category."
    )
    @PostMapping("/addExpense")
    public ResponseEntity<ExpenseResponse> addExpensse(@Valid @RequestBody ExpenseRequest expenseRequest) {
        return new ResponseEntity<>(expenseService.addExpense(expenseRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "View all expenses (paginated)",
            description = "Returns paginated list of all expenses with sorting support. Usually for admin/report views."
    )
    @GetMapping("/viewExpenses")
    public ResponseEntity<Page<ExpenseResponse>> viewExpenses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(defaultValue = "expenseID") String sortBy) {
        return new ResponseEntity<>(expenseService.viewAllExpense(page, size, sortBy), HttpStatus.OK);
    }

    @Operation(
            summary = "View expenses of a user (paginated)",
            description = "Returns paginated expenses for the given userId with sorting."
    )
    @GetMapping("/users/{userId}/expenses")
    public Page<ExpenseResponse> getUserExpenses(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "expenseID") String sortBy) {

        return expenseService.viewAllUserExpense(userId, page, size, sortBy);
    }

    @Operation(
            summary = "Update an existing expense",
            description = "Updates title, amount, category and date for the selected expense."
    )
    @PutMapping("/updateExpenses/{userID}")
    public ResponseEntity<ExpenseResponse> updateExpenses(@PathVariable long userID, @Valid @RequestBody ExpenseRequest expenseRequest) {
        return new ResponseEntity<>(expenseService.updateExpense(userID, expenseRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an expense",
            description = "Removes a single expense based on expenseID."
    )
    @DeleteMapping("/deleteExpense/{expenseID}")
    public ResponseEntity<String> deleteExpense(@PathVariable long expenseID) {
        expenseService.deleteExpense(expenseID);
        return new ResponseEntity<>("Expense Delete Successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Delete all expenses",
            description = "Deletes all expense records from the system (use cautiously)."
    )
    @DeleteMapping("/deleteAllExpense")
    public ResponseEntity<String> deleteAllExpense() {
        expenseService.deleteAll();
        return new ResponseEntity<>("All Expense Delete Successfully", HttpStatus.OK);
    }

    @Operation(summary = "Get total expense for selected month",
            description = "Returns total expense for a user for a given year and month")
    @GetMapping("/users/{userId}/expenses/total")
    public Double getMonthlyTotal(@PathVariable Long userId, @RequestParam int year, @RequestParam int month) {
        return expenseService.getMonthlyTotal(userId, year, month);
    }

    @Operation(
            summary = "Get category-wise spending",
            description = "Returns total amount spent per category for the given user."
    )
    @GetMapping("/users/{userId}/expenses/category-summary")
    public List<CategorySpendingResponse> getCategorySpending(@PathVariable Long userId) {
        return expenseService.getCategoryWiseSpending(userId);
    }

    @Operation(summary = "Get monthly trend report for user")
    @GetMapping("/users/{userId}/expenses/monthly-report")
    public List<MonthlyTrendReport> getMonthlyTrendReport(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "false") boolean includeCategory
    ) {
        return expenseService.getMonthlyTrendReport(userId, includeCategory);
    }


}
