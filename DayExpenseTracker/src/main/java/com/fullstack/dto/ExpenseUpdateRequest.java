package com.fullstack.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ExpenseUpdateRequest {

    @NotEmpty
    private String title;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    private String categoryName;

    @NotNull
    private LocalDate date;
}
