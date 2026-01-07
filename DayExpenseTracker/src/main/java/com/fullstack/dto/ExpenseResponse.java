package com.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    private long expenseID;

    private String title;

    private double amount;

    private String categoryName;

    private String massage;

    private Date date;
}
