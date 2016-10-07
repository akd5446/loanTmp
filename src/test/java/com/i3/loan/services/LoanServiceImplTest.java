package com.i3.loan.services;

import com.i3.loan.common.exceptions.BadRequestException;
import com.i3.loan.common.exceptions.NotFoundException;
import com.i3.loan.models.Book;
import com.i3.loan.models.Employee;
import com.i3.loan.models.Loan;
import com.i3.loan.repositories.BookRepository;
import com.i3.loan.repositories.EmployeeRepository;
import com.i3.loan.repositories.LoanRepository;
import com.i3.loan.services.impl.LoanServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void canTakeOutNewLoan() {
        // Given
        Book book = new Book("foo", 3);
        Employee employee = new Employee("foo");

        given(bookRepository.findOne(book.getId())).willReturn(book);
        given(employeeRepository.findOne(employee.getId())).willReturn(employee);

        // When
        loanService.loanOutBook(employee, book);

        // Then
        verify(loanRepository).save(new Loan(employee, book));
    }

    @Test(expected = BadRequestException.class)
    public void cannotTakeOutLoanIfAllCopiesAlreadyLoaned() {
        // Given
        Book book = new Book("foo", 3);
        Employee employee = new Employee("foo");

        given(bookRepository.findOne(book.getId())).willReturn(book);
        given(employeeRepository.findOne(employee.getId())).willReturn(employee);
        given(loanRepository.countByBook(book)).willReturn(3L);

        // When
        loanService.loanOutBook(employee, book);
    }
}