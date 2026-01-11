package com.fullstack.repository;

import com.fullstack.entity.Category;
import com.fullstack.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCaseAndUsers_Id(String name, Long userId);

    long deleteByIdAndUsers_Id(Long categoryId, Long userId);

    List<Category> findByUsers(Users user);

    List<Category> findByUsers_Id(Long userId);

    List<Category> findByUsersAndIsDeletedFalse(Users users);

    List<Category> findByUsers_IdAndIsDeletedFalse(Long userId);
}
