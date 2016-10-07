package com.i3.loan.repositories;

import com.i3.loan.models.Book;
import com.i3.loan.models.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    List<Loan> findAll();

    long countByBook(Book book);

    @Query("SELECT l FROM Loan l WHERE l.returnDate < CURRENT_DATE")
    List<Loan> findOverdue();
}
