package com.i3.loan.services;

import com.i3.loan.common.exceptions.BadRequestException;
import com.i3.loan.models.Book;
import com.i3.loan.models.Employee;
import com.i3.loan.models.Loan;

public interface LoanService {
    Loan loanOutBook(Employee employee, Book book) throws BadRequestException;
}
