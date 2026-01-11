package com.fullstack.controller;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.dto.CustomApiResponse;
import com.fullstack.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Categories", description = "APIs for category management")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category",
            description = "Adds a new expense category for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.crateCategory(categoryRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple categories",
            description = "Adds multiple expense categories for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categories created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/createCategories")
    public ResponseEntity<List<CategoryResponse>> createCategories(@Valid @RequestBody List<CategoryRequest> categoryRequest) {
        return new ResponseEntity<>(categoryService.crateCategories(categoryRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories for the logged-in user",
            description = "Returns all categories created by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/getUserCategory")
    public ResponseEntity<List<CategoryResponse>> getUserCategory() {
        return new ResponseEntity<>(categoryService.getUserCategory(), HttpStatus.OK);
    }

    @Operation(summary = "Soft delete a category",
            description = "Soft deletes a category by ID. The category is not permanently removed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.softDeleteCategory(id);
        CustomApiResponse<Void> response = new CustomApiResponse<>(
                "SUCCESS",
                "Category deleted successfully",
                LocalDateTime.now(),
                null,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
