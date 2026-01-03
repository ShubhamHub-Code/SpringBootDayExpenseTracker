package com.fullstack.service;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;

import java.util.List;

public interface CategoryService {

    CategoryResponse crateCategory(CategoryRequest categoryRequest);

    List<CategoryResponse>getUserCategory(long categoryID);

    void deleteCategory(long userID);

    void deleteAllCategory();

}
