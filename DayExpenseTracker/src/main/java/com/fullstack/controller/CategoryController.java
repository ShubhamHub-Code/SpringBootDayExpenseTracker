package com.fullstack.controller;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;
import com.fullstack.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "APIs for category management")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Adds a new expense category for the logged-in user."
    )
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse > create(@Valid @RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.crateCategory(categoryRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get categories for a user",
            description = "Returns all categories created by a specific user."
    )
    @GetMapping("/getUserCategory/{userId}")
    public ResponseEntity<List<CategoryResponse>> getUserCategory(@PathVariable long userId) {
        return new ResponseEntity<>(categoryService.getUserCategory(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete category",
            description = "Deletes a specific category using category ID."
    )
    @DeleteMapping("/deleteCategory/{categoryID}")
    public ResponseEntity<String> deleteCategory(@PathVariable long categoryID) {
        categoryService.deleteCategory(categoryID);
        return new ResponseEntity<>("Catagory delete sucessfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Delete all categories",
            description = "Deletes all categories from the system. Use carefully."
    )
    @DeleteMapping("/deleteAllCategory")
    public ResponseEntity<String> deleteAllCategory() {
        categoryService.deleteAllCategory();
        return new ResponseEntity<>("All Catagory are deleted", HttpStatus.OK);
    }
}
