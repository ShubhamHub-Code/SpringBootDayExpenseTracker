package com.fullstack.controller;

import com.fullstack.dto.*;
import com.fullstack.entity.Users;
import com.fullstack.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Users", description = "APIs for user management")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with basic details such as name, email, and password."
    )
    @PostMapping("/registerUser")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Fetches a single user using the provided userID."
    )
    @GetMapping("/getUserById/{UserID}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable long UserID) {
        return new ResponseEntity<>(userService.getUserByID(UserID), HttpStatus.OK);
    }

    @Operation(
            summary = "Get list of all users",
            description = "Fetches and returns all registered users from the system."
    )
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update user details",
            description = "Updates name, email, mobile or other information for a specific user."
    )
    @PutMapping("/updateUser/{UserID}")
    public ResponseEntity<Users> updateUser(@PathVariable long UserID, @RequestBody Users users) {
        return new ResponseEntity<>(userService.Update(UserID, users), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a specific user based on the userID."
    )
    @DeleteMapping("/deleteUser/{UserID}")
    public ResponseEntity<String> deleteUser(@PathVariable long UserID) {
        userService.deleteUserByID(UserID);
        return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Delete all users",
            description = "Removes all users from the system. Use only for admin/testing."
    )
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser() {
        userService.deleteAll();
        return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates the user using email and password and returns login details."
    )
    @PostMapping("/loginUser")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest logInRequest) {
        return new ResponseEntity<>(userService.signin(logInRequest), HttpStatus.OK);
    }
}
