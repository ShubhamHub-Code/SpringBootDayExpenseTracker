package com.fullstack.controller;

import com.fullstack.dto.*;
import com.fullstack.entity.Users;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.impl.CategoryServiceImpl;
import com.fullstack.service.impl.ExpenseServiceImpl;
import com.fullstack.service.impl.UserServiceImpl;
import com.fullstack.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Access", description = "APIs for Admin Management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;
    private final ExpenseServiceImpl expenseService;
    private final UserRepository userRepository;

    @Operation(summary = "Get list of all users by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin access required")
    })
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {
        SecurityUtil.checkAdminAccess();
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Get a specific user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable long userId) {
        SecurityUtil.checkAdminAccess();
        return ResponseEntity.ok(userService.getUserByID(userId));
    }

    @Operation(summary = "Update user details by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @PathVariable long userId) {
        SecurityUtil.checkAdminAccess();
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));
        UserResponse updatedUser = userService.Update(userId, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete all users by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser() {
        SecurityUtil.checkAdminAccess();
        userService.deleteAll();
        return ResponseEntity.ok("All users deleted successfully");
    }

    @Operation(summary = "Delete a specific user by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) {
        SecurityUtil.checkAdminAccess();
        userService.deleteCurrentUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Delete all expenses for a user by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All expenses deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/deleteAllExpense/{userId}")
    public ResponseEntity<Map<String, String>> deleteAllExpense(@PathVariable long userId) {
        SecurityUtil.checkAdminAccess();
        expenseService.deleteAllAdmin(userId);
        return ResponseEntity.ok(Map.of("message", "All expenses deleted successfully"));
    }

    @Operation(summary = "Get all categories for a user by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/getUserCategory/{userId}")
    public ResponseEntity<List<CategoryResponse>> getUserCategory(@PathVariable long userId) {
        SecurityUtil.checkAdminAccess();
        return ResponseEntity.ok(categoryService.getUserCategory());
    }

    @Operation(summary = "Delete a specific category for a user by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/deleteCategory/{categoryId}/{userId}")
    public ResponseEntity<String> deleteCategory(@PathVariable long categoryId, @PathVariable long userId) {
        SecurityUtil.checkAdminAccess();
        categoryService.deleteCategory(categoryId, userId);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @Operation(summary = "Delete all categories by Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All categories deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/deleteAllCategory")
    public ResponseEntity<Map<String, String>> deleteAllCategory() {
        SecurityUtil.checkAdminAccess();
        categoryService.deleteAllCategory();
        return ResponseEntity.ok(Map.of("message", "All categories deleted successfully"));
    }

    @Operation(summary = "View all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/viewAllCategory")
    public ResponseEntity<List<CategoryResp>> viewAllCategory() {
        SecurityUtil.checkAdminAccess();
        return ResponseEntity.ok(categoryService.viewAllCategory());
    }

    @Operation(summary = "View all categories along with user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories with user info retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/viewAllCategorywithUser")
    public ResponseEntity<List<CategoryResponse>> viewAllCategorywithUser() {
        SecurityUtil.checkAdminAccess();
        return ResponseEntity.ok(categoryService.viewAllCategorywithUser());
    }
}
