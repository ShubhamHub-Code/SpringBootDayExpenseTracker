
package com.fullstack.controller;

import com.fullstack.dto.UserRequest;
import com.fullstack.entity.Users;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.impl.CategoryServiceImpl;
import com.fullstack.service.impl.ExpenseServiceImpl;
import com.fullstack.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Admin Access", description = "APIs for Admin Management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserServiceImpl userService;

    private final CategoryServiceImpl categoryService;

    private final ExpenseServiceImpl expenseService;

    private final UserRepository userRepository;

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String role = auth.getAuthorities().toString();

    @Operation(summary = "Get list of all users",
            description = "Fetches and returns all registered users from the system.")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {

        if (role.equalsIgnoreCase("ADMIN")) {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    @Operation(summary = "Get user by ID",
            description = "Fetches a single user using the provided userID.")
    @GetMapping("/getUserById/{UserID}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable long UserID) {
        return new ResponseEntity<>(userService.getUserByID(UserID), HttpStatus.OK);
    }


    @Operation(summary = "Update user details",
            description = "Updates name, email, mobile or other information for a specific user.")
    @PutMapping("/updateUser")
    public ResponseEntity<Users> updateUser(@Valid @RequestBody UserRequest userRequest, @PathVariable long userId) {

        if (role.equalsIgnoreCase("ADMIN")) {

            Users existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

            if (userRequest.getName() != null) {
                existingUser.setName(userRequest.getName());
            }
            if (userRequest.getEmail() != null) {
                existingUser.setEmail(userRequest.getEmail());
            }
            if (userRequest.getPassword() != null) {
                existingUser.setPassword(userRequest.getPassword());
            }

            Users updatedUser = userService.Update(userId, userRequest);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    @Operation(summary = "Delete all users",
            description = "Removes all users from the system. Use only for admin/testing.")
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser() {

        if (role.equalsIgnoreCase("ADMIN")) {
            userService.deleteAll();
            return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    @Operation(summary = "Delete user",
            description = "Deletes a specific user based on the userID.")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(long userId) {

        if (role.equalsIgnoreCase("ADMIN")) {
            userService.deleteCurrentUser(userId);
            return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    @Operation(summary = "Delete all expenses",
            description = "Deletes all expense records from the system (use cautiously).")
    @DeleteMapping("/deleteAllExpense/{userID}")
    public ResponseEntity<Map<String, String>> deleteAllExpense(long userID) {

        if (role.equalsIgnoreCase("ADMIN")) {
            expenseService.deleteAllAdmin(userID);
            Map<String, String> response = new HashMap<>();
            response.put("message", "All Expense deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }


    }
}

