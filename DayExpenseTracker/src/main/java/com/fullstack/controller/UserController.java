package com.fullstack.controller;

import com.fullstack.dto.LogInRequest;
import com.fullstack.dto.LogInResponse;
import com.fullstack.dto.UserRequestDto;
import com.fullstack.entity.Users;
import com.fullstack.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Day-to-Day Expense Tracker", description = "APIS Of User Controller")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/registerUser")
    public ResponseEntity<Users> register(@Valid @RequestBody UserRequestDto user) {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/getUserById/{UserID}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable long UserID) {
        return new ResponseEntity<>(userService.getUserByID(UserID), HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/updateUser/{UserID}")
    public ResponseEntity<Users> updateUser(@PathVariable long UserID, @RequestBody Users users) {
        return new ResponseEntity<>(userService.Update(UserID, users), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{UserID}")
    public ResponseEntity<String> deleteUser(@PathVariable long UserID) {
        userService.deleteUserByID(UserID);
        return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<String> deleteAllUser() {
        userService.deleteAll();
        return new ResponseEntity<>("User Account Deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest logInRequest) {
        return new ResponseEntity<>(userService.signin(logInRequest), HttpStatus.OK);
    }
}
