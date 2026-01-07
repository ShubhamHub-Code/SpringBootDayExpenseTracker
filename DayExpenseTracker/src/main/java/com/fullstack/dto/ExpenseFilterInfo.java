package com.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseFilterInfo {

    private LocalDate fromDate;

    private LocalDate toDate;

    private List<Long> categoryIds;

    private Double minAmount;

    private Double maxAmount;
}
