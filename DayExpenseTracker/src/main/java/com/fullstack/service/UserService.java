package com.fullstack.service;

import com.fullstack.dto.*;
import com.fullstack.entity.Users;
import org.apache.catalina.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    UserResponse registerUser(UserRequest request);

    LogInResponse signin(LogInRequest logInRequest);

    Optional<Users> getUserByID(long userID);

    List<Users> findAll();

    Users Update(long userID, Users users);

    void deleteUserByID(long userID);

    void deleteAll();
}
