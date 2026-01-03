package com.fullstack.service.impl;

import com.fullstack.dto.LogInRequest;
import com.fullstack.dto.LogInResponse;
import com.fullstack.dto.UserRequestDto;
import com.fullstack.entity.Users;
import com.fullstack.exception.EmailAlreadyExistsException;
import com.fullstack.exception.UserNotFoundException;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Override
    public Users registerUser(UserRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        Users users = new Users();
        users.setName(request.getName());
        users.setEmail(request.getEmail());
        // users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setPassword(request.getPassword());
        return userRepository.save(users);
    }

    @Override
    public LogInResponse signin(LogInRequest logInRequest) {

        Users users = userRepository.findByEmail(logInRequest.email());

        if (users == null) {
            throw new UserNotFoundException("USER NOT FOUND");
        }
        LogInResponse logInResponse = new LogInResponse();
        if (users.getPassword().equals(logInRequest.password())) {

            logInResponse.setUserEmail(users.getEmail());
            logInResponse.setUserName(users.getName());
            logInResponse.setUserID(users.getId());
            logInResponse.setMessage(users.getEmail()+" login successfully");

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
    public Users Update(long userID, Users users) {

        Users users1 = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User #ID not found"));
        users1.setName(users.getName());
        users1.setEmail(users.getEmail());
        users1.setPassword(users.getPassword());

        return userRepository.save(users1);
    }

    @Override
    public void deleteUserByID(long userID) {
        userRepository.deleteById(userID);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }


}
