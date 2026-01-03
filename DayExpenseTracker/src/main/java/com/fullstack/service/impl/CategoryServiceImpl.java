package com.fullstack.service.impl;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;
import com.fullstack.entity.Users;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.CategoryRepository;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;


    @Override
    public CategoryResponse crateCategory(CategoryRequest categoryRequest) {

        Users users = userRepository.findById(categoryRequest.getUserID()).orElseThrow(() -> new UserNotFoundException("USER #ID NOT FOUND"));

        categoryRepository.findByNameAndUsers_Id(categoryRequest.getName(), categoryRequest.getUserID())
                .ifPresent(c -> {
                    throw new RuntimeException("Category already exists");
                });

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setUsers(users);

        Category saved = categoryRepository.save(category);

        CategoryResponse response = new CategoryResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setUserID(saved.getUsers().getId());

        return response;
    }

    @Override
    public List<CategoryResponse> getUserCategory(long userID) {
        return categoryRepository.findAll()
                .stream()
                .filter(c -> c.getUsers().getId().equals(userID))
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .description(c.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(long categoryID) {
        categoryRepository.deleteById(categoryID);
    }

    @Override
    public void deleteAllCategory() {
        categoryRepository.deleteAll();
    }
}
