package com.fullstack.service;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse crateCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> crateCategories(List<CategoryRequest> categoryRequest);

    List<CategoryResponse> getUserCategory();

    void deleteCategory(long userID);

    void deleteAllCategory();

    long getLoggedInUserId();
}
