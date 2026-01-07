package com.fullstack.repository;

import com.fullstack.dto.CategorySpendingResponse;
import com.fullstack.dto.MonthlyExpenseTrend;
import com.fullstack.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUser_Id(Long userId, Pageable pageable);

   // Page<Expense> findAll(int page, int size, String sortBy);

    @Query("""
       SELECT COALESCE(SUM(e.expenseAmount), 0)
       FROM Expense e
       WHERE e.user.id = :userId
         AND e.expenseDate BETWEEN :startDate AND :endDate
       """)
    Double getTotalExpenseForMonth(Long userId, LocalDate startDate, LocalDate endDate);


    @Query("""
       SELECT new com.fullstack.dto.CategorySpendingResponse(
           e.category.name,
           COALESCE(SUM(e.expenseAmount), 0)
       )
       FROM Expense e
       WHERE e.user.id = :userId
       GROUP BY e.category.name
       """)
    List<CategorySpendingResponse> getCategoryWiseSpending(Long userId);


    @Query("""
       SELECT new com.fullstack.dto.MonthlyExpenseTrend(
           YEAR(e.expenseDate),
           MONTH(e.expenseDate),
           COALESCE(SUM(e.expenseAmount), 0)
       )
       FROM Expense e
       WHERE e.user.id = :userId
       GROUP BY YEAR(e.expenseDate), MONTH(e.expenseDate)
       ORDER BY YEAR(e.expenseDate), MONTH(e.expenseDate)
       """)
    List<MonthlyExpenseTrend> getMonthlyTrend(Long userId);

    @Query("""
       SELECT new com.fullstack.dto.CategorySpendingResponse(
           e.category.name,
           COALESCE(SUM(e.expenseAmount), 0)
       )
       FROM Expense e
       WHERE e.user.id = :userId
         AND YEAR(e.expenseDate) = :year
         AND MONTH(e.expenseDate) = :month
       GROUP BY e.category.name
       """)
    List<CategorySpendingResponse> getCategorySpendingForMonth(Long userId, int year, int month);

    void deleteByUser_Id(Long userId);
}
