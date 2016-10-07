package com.i3.loan.controllers;

import com.i3.loan.common.exceptions.NotFoundException;
import com.i3.loan.models.Book;
import com.i3.loan.models.Employee;
import com.i3.loan.models.Loan;
import com.i3.loan.models.dto.LoanCreationDto;
import com.i3.loan.repositories.BookRepository;
import com.i3.loan.repositories.EmployeeRepository;
import com.i3.loan.repositories.LoanRepository;
import com.i3.loan.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    @RequestMapping(path = "/{id}", method = GET)
    public Loan get(@PathVariable long id) {
        Loan loan = loanRepository.findOne(id);

        if (loan == null) {
            throw new NotFoundException(String.format("No loan with ID: %s", id));
        }

        return loan;
    }

    @RequestMapping(method = GET)
    public List<Loan> get() {
        return loanRepository.findAll();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Loan create(@RequestBody LoanCreationDto loanCreation) {
        Employee employee = employeeRepository.findOne(loanCreation.getEmployeeId());
        if (employee == null) {
            throw new NotFoundException(String.format("No employee with ID: %s", loanCreation.getEmployeeId()));
        }

        Book book = bookRepository.findOne(loanCreation.getBookId());
        if (book == null) {
            throw new NotFoundException(String.format("No book with ID: %s", loanCreation.getBookId()));
        }

        return loanService.loanOutBook(employee, book);
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    public void delete(@PathVariable long id) {
        loanRepository.delete(id);
    }
}
