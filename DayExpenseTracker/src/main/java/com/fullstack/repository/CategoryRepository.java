package com.fullstack.repository;

import com.fullstack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameAndUsers_Id(String name, Long userId);

    Optional<Category>findByName(String name);
}
