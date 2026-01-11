package com.fullstack.service;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResp;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;
import com.fullstack.entity.Users;

import java.util.List;

public interface CategoryService {

    CategoryResponse crateCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> crateCategories(List<CategoryRequest> categoryRequest);

    List<CategoryResponse> getUserCategory();

    List<CategoryResp> viewAllCategory();

    public List<CategoryResponse> viewAllCategorywithUser();

    void deleteCategory(long userID);

    void deleteAllCategory();

    void deleteCategory(Long categoryId, Long userId);

    long getLoggedInUserId();

    void softDeleteCategory(Long categoryId);
}
