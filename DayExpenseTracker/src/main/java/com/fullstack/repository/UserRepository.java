package com.fullstack.repository;

import com.fullstack.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Users findByEmail(String email);

    boolean existsByEmail(String email);

    Users findByEmailAndPassword(String email, String password);
}
