package com.fullstack.controller;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;
import com.fullstack.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse > create(@Valid @RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.crateCategory(categoryRequest), HttpStatus.CREATED);
    }

    @GetMapping("/getUserCategory/{userId}")
    public ResponseEntity<List<CategoryResponse>> getUserCategory(@PathVariable long userId) {
        return new ResponseEntity<>(categoryService.getUserCategory(userId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteCategory/{categoryID}")
    public ResponseEntity<String> deleteCategory(@PathVariable long categoryID) {
        categoryService.deleteCategory(categoryID);
        return new ResponseEntity<>("Catagory delete sucessfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllCategory")
    public ResponseEntity<String> deleteAllCategory() {
        categoryService.deleteAllCategory();
        return new ResponseEntity<>("All Catagory are deleted", HttpStatus.OK);
    }
}
