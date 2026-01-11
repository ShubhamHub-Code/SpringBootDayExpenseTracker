package com.fullstack.service.impl;

import com.fullstack.dto.CategoryRequest;
import com.fullstack.dto.CategoryResp;
import com.fullstack.dto.CategoryResponse;
import com.fullstack.entity.Category;
import com.fullstack.entity.Users;
import com.fullstack.exception.ResourceAlreadyExistsException;
import com.fullstack.exception.ResourceNotFoundException;
import com.fullstack.exception.UnauthorizedException;
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

        categoryRepository.findByNameIgnoreCaseAndUsers_Id(categoryRequest.getName(), userId)
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
        response.setUserName(users.getName());
        response.setUserEmail(users.getEmail());

        return response;
    }

    public long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("USER NOT FOUND"));
        return user.getId();
    }

    @Override
    public void softDeleteCategory(Long categoryId) {
        Long userId = getLoggedInUserId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Users currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Users owner = category.getUsers();
        if (owner == null || (!owner.getId().equals(currentUser.getId()) && !currentUser.isAdmin())) {
            throw new UnauthorizedException("Not allowed to delete this category");
        }

        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponse> crateCategories(List<CategoryRequest> categoryRequest) {

        log.info("@@@@@@@Create new list of Categories");
        List<Category> categoriesToSave = new ArrayList<>();
        List<CategoryResponse> responses = new ArrayList<>();

        Long userId = getLoggedInUserId();
        Users users = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("USER #ID NOT FOUND"));
        for (CategoryRequest request : categoryRequest) {

            categoryRepository.findByNameIgnoreCaseAndUsers_Id(request.getName(), userId)
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
            response.setUserName(users.getName());
            response.setUserEmail(users.getEmail());
            responses.add(response);
        }

        return responses;
    }

    @Override
    public List<CategoryResponse> getUserCategory() {
        Long userId = getLoggedInUserId();

        List<Category> categories = categoryRepository.findByUsers_IdAndIsDeletedFalse(userId);

        return categories.stream()
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .description(c.getDescription())
                        .userID(c.getUsers().getId())  // get ID from Users object
                        .build())
                .toList();
    }

    @Override
    public List<CategoryResp> viewAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(cat -> {
            CategoryResp resp = new CategoryResp();
            resp.setId(cat.getId());
            resp.setName(cat.getName());
            resp.setDescription(cat.getDescription());

            return resp;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> viewAllCategorywithUser() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(cat -> {
            CategoryResponse resp = new CategoryResponse();
            resp.setId(cat.getId());
            resp.setName(cat.getName());
            resp.setDescription(cat.getDescription());

            if (cat.getUsers() != null) {
                resp.setUserID(cat.getUsers().getId());
                resp.setUserName(cat.getUsers().getName());
                resp.setUserEmail(cat.getUsers().getEmail());
            }

            return resp;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(long categoryID) {
        categoryRepository.deleteById(categoryID);
    }

    @Override
    public void deleteCategory(Long categoryId, Long userId) {

        long deleted = categoryRepository.deleteByIdAndUsers_Id(categoryId, userId);

        if (deleted == 0) {
            throw new ResourceNotFoundException("Category not found for this user");
        }
    }

    @Override
    public void deleteAllCategory() {
        categoryRepository.deleteAll();
    }
}
