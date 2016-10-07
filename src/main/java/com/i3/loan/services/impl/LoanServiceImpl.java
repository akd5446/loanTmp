package com.i3.loan.services.impl;

import com.i3.loan.common.exceptions.BadRequestException;
import com.i3.loan.common.exceptions.NotFoundException;
import com.i3.loan.models.Book;
import com.i3.loan.models.Employee;
import com.i3.loan.models.Loan;
import com.i3.loan.repositories.BookRepository;
import com.i3.loan.repositories.EmployeeRepository;
import com.i3.loan.repositories.LoanRepository;
import com.i3.loan.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl
        implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Override
    public Loan loanOutBook(final Employee employee, final Book book)
            throws BadRequestException, NotFoundException {

        long currentLoans = loanRepository.countByBook(book);
        if (book.getCopies() <= currentLoans) {
            throw new BadRequestException("All copies of book are loaned out");
        }

        return loanRepository.save(new Loan(employee, book));
    }
}
