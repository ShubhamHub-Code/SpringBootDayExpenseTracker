package com.fullstack.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;

@Data
public class ExpenseRequest {

    @NotEmpty
    private String title;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    private String categoryName;

    @NotNull
    private Long userId;

    @NotNull
    private Date date;
}
