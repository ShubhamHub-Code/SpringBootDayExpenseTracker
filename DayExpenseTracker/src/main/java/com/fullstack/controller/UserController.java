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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Tag(name = "Users", description = "APIs for user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserServiceImpl userService;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    @Operation(summary = "Register a new user",
            description = "Creates a new user account with basic details such as name, email, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user details")
    })
    @PostMapping("/register")
    public ResponseEntity<CustomApiResponse<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        log.info("Received request to register new user: {}", request.getName());
        UserResponse createdUser = userService.registerUser(request);
        CustomApiResponse<UserResponse> customApiResponse = new CustomApiResponse<>(
                "Success", "User registered successfully", LocalDateTime.now(), createdUser, null
        );
        return new ResponseEntity<>(customApiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user details",
            description = "Updates name, email, mobile or other information for the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update")
    public ResponseEntity<CustomApiResponse<UserResponse>> updateUser(@Valid @RequestBody UserRequest userRequest) {
        long userId = categoryService.getLoggedInUserId();
        log.info("Received request to update user with id: {}", userId);
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponse updatedUser = userService.Update(userId, userRequest);
        CustomApiResponse<UserResponse> response = new CustomApiResponse<>(
                "Success", "User updated successfully", LocalDateTime.now(), updatedUser, null
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete user",
            description = "Deletes the currently logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<CustomApiResponse<Void>> deleteUser() {
        long userId = categoryService.getLoggedInUserId();
        log.info("Received request to delete user with id: {}", userId);
        userService.deleteCurrentUser(userId);
        CustomApiResponse<Void> response = new CustomApiResponse<>(
                "Success", "User deleted successfully", LocalDateTime.now(), null, null
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login user",
            description = "Authenticates the user using email and password and returns JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<LogInResponse>> login(@Valid @RequestBody LogInRequest request) {
        log.info("Received login request for user: {}", request.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Users user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        LogInResponse loginResponse = new LogInResponse();
        loginResponse.setUserID(user.getId());
        loginResponse.setUserName(user.getName());
        loginResponse.setUserEmail(user.getEmail());
        loginResponse.setMessage("Login successful");
        loginResponse.setToken(token);

        CustomApiResponse<LogInResponse> customApiResponse = new CustomApiResponse<>(
                "Success", "User login successful", LocalDateTime.now(), loginResponse, null
        );

        log.info("Login successful for user: {}", user.getName());
        return ResponseEntity.ok(customApiResponse);
    }
}
