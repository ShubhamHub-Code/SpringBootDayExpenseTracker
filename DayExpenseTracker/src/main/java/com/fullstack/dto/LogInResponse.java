package com.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponse {

    private long userID;

    private String userName;

    private String userEmail;

    private String message;

    private String token;
}
