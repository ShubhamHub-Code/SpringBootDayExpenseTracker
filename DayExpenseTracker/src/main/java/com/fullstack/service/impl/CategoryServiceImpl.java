package com.fullstack.service.impl;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;
import com.fullstack.entity.Users;
import com.fullstack.exception.ResourceAlreadyExistsException;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.CategoryRepository;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;


    @Transactional
    @Override
    public CategoryResponse crateCategory(CategoryRequest categoryRequest) {

        log.info("@@@@@@@Create new Category of : "+categoryRequest.getName());

        Long userId = getLoggedInUserId();

        Users users = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("USER #ID NOT FOUND"));

        categoryRepository.findByNameAndUsers_Id(categoryRequest.getName(), userId)
                .ifPresent(c -> {
                    throw new ResourceAlreadyExistsException("Category already exists");
                });

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setUsers(users);

        log.info("Saved category : "+category.getName());

        Category saved = categoryRepository.save(category);

        CategoryResponse response = new CategoryResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setUserID(saved.getUsers().getId());

        return response;
    }

    public long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Users user = userRepository.findByEmail(email);

        return user.getId();
    }

    @Override
    public List<CategoryResponse> crateCategories(List<CategoryRequest> categoryRequest) {

        log.info("@@@@@@@Create new list of Categories");
        List<Category> categoriesToSave = new ArrayList<>();
        List<CategoryResponse> responses = new ArrayList<>();

        Long userId = getLoggedInUserId();

        for (CategoryRequest request : categoryRequest) {

            Users users = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("USER #ID NOT FOUND"));

            categoryRepository.findByNameAndUsers_Id(request.getName(), userId)
                    .ifPresent(c -> {
                        throw new RuntimeException("Category already exists");
                    });

            Category category = new Category();
            category.setName(request.getName());
            category.setDescription(request.getDescription());
            category.setUsers(users);

            log.info("Saved category for : "+category.getName());

            categoriesToSave.add(category);
        }

        List<Category> savedCategories = categoryRepository.saveAll(categoriesToSave);

        for (Category category : savedCategories) {

            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            response.setDescription(category.getDescription());
            response.setUserID(category.getUsers().getId());

            responses.add(response);
        }

        return responses;
    }

    @Override
    public List<CategoryResponse> getUserCategory() {

        Long userId = getLoggedInUserId();

        return categoryRepository.findAll()
                .stream()
                .filter(c -> c.getUsers() != null && c.getUsers().getId().equals(userId))
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .description(c.getDescription())
                        .userID(c.getUsers().getId())
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
