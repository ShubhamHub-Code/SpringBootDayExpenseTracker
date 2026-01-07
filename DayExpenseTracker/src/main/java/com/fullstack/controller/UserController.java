package com.fullstack.controller;

import com.fullstack.dto.*;
import com.fullstack.entity.Users;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.CategoryService;
import com.fullstack.service.impl.CustomUserDetailsService;
import com.fullstack.service.impl.UserServiceImpl;
import com.fullstack.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Users", description = "APIs for user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Auth")
public class UserController {

    private final UserServiceImpl userService;

    private final JWTUtil jwtUtil;

    private final CustomUserDetailsService customUserService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final CategoryService categoryService;

    @Operation(summary = "Register a new user",
            description = "Creates a new user account with basic details such as name, email, and password.")
    @PostMapping("/registerUser")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update user details",
            description = "Updates name, email, mobile or other information for a specific user.")
    @PutMapping("/updateUser")
    public ResponseEntity<Users> updateUser(@Valid @RequestBody UserRequest userRequest) {

        long userId = categoryService.getLoggedInUserId();

        Users existingUser = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));

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
    }

    @Operation(summary = "Delete user",
            description = "Deletes a specific user based on the userID.")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser() {

        long userId = categoryService.getLoggedInUserId();
        userService.deleteCurrentUser(userId);
        return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
    }

    @Operation(summary = "Login user",
            description = "Authenticates the user using email and password and returns login details.")
    @PostMapping("/loginUser")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        Users user = userRepository.findByEmail(request.email());

        String token = jwtUtil.generateToken(user.getEmail());

        LogInResponse response = new LogInResponse();
        response.setUserID(user.getId());
        response.setUserName(user.getName());
        response.setUserEmail(user.getEmail());
        response.setMessage("Login successful");
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get list of all users",
            description = "Fetches and returns all registered users from the system.")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        if (role.equalsIgnoreCase("ADMIN")) {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

    }

    @Operation(summary = "Delete all users",
            description = "Removes all users from the system. Use only for admin/testing.")
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        if (role.equalsIgnoreCase("ADMIN")) {
            userService.deleteAll();
            return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }


    }
}
