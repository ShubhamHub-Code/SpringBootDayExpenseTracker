package com.fullstack.service;

import com.fullstack.dto.LogInRequest;
import com.fullstack.dto.LogInResponse;
import com.fullstack.dto.UserRequestDto;
import com.fullstack.entity.Users;
import org.apache.catalina.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Users registerUser(UserRequestDto request);

    LogInResponse signin(LogInRequest logInRequest);

    Optional<Users> getUserByID(long userID);

    List<Users> findAll();

    Users Update(long userID, Users users);

    void deleteUserByID(long userID);

    void deleteAll();
}
