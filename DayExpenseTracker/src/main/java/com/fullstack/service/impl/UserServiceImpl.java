package com.fullstack.service.impl;

import com.fullstack.dto.*;
import com.fullstack.entity.Users;
import com.fullstack.enums.Role;
import com.fullstack.exception.EmailAlreadyExistsException;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponse registerUser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.info("{} Account already present",request.getName());
            throw new EmailAlreadyExistsException("Email already registered");
        }

        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.USER);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users saved = userRepository.save(user);

        log.info("{} account register successfully",user.getName());
        UserResponse response = new UserResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setMessage("User registered successfully");

        return response;
    }

    @Override
    public LogInResponse signin(LogInRequest logInRequest) {

        Users users = userRepository.findByEmail(logInRequest.email()).orElseThrow(()-> new UserNotFoundException("USER NOT FOUND"));

        if (users == null) {
            throw new UserNotFoundException("USER NOT FOUND");
        }
        LogInResponse logInResponse = new LogInResponse();
        if (users.getPassword().equals(logInRequest.password())) {

            logInResponse.setUserEmail(users.getEmail());
            logInResponse.setUserName(users.getName());
            logInResponse.setUserID(users.getId());
            logInResponse.setMessage(users.getEmail() + " login successfully");

        }
        return logInResponse;
    }

    @Override
    public Optional<Users> getUserByID(long userID) {
        return Optional.of(userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User #ID not found")));
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserResponse Update(long userID, UserRequest request) {
        Users existingUser = userRepository.findById(userID).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));

        // Update only non-null fields from DTO
        if (request.getName() != null){
            existingUser.setName(request.getName());
        }
        if (request.getEmail() != null){
            existingUser.setEmail(request.getEmail());
        }
        if (request.getPassword() != null){
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Users saved = userRepository.save(existingUser);

        log.info("{} account updated successfully",saved.getName());
        UserResponse response = new UserResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setMessage("User Updated successfully");

        return response;
    }


    @Override
    public void deleteCurrentUser(long userID) {
        Users existingUser = userRepository.findById(userID).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
        userRepository.delete(existingUser);
        log.info("{} account delete successfully",existingUser.getName());
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }


}
