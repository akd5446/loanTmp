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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoanControllerTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @Test
    public void canGetById() {
        // Given
        Employee employee = new Employee("foo");
        Book book = new Book("foo", 5);
        Loan expected = new Loan(employee, book);

        given(loanRepository.findOne(123L)).willReturn(expected);

        // When
        Loan actual = loanController.get(123L);

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NotFoundException.class)
    public void throwsNotFoundWhenRetrievingNonExistentLoan() {
        // When
        loanController.get(123L);
    }

    @Test
    public void canGetListOfLoans() {
        // Given
        Employee employee1 = new Employee("foo");
        Employee employee2 = new Employee("bar");
        Book book1 = new Book("foo", 5);
        Book book2 = new Book("bar", 3);

        Loan[] expected = {new Loan(employee1, book1), new Loan(employee1, book2), new Loan(employee2, book1)};
        given(loanRepository.findAll()).willReturn(Arrays.asList(expected));

        // When
        List<Loan> actual = loanController.get();

        // Then
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void getListEmptyIfNoLoansExist() {
        // When
        List<Loan> actual = loanController.get();

        // Then
        assertThat(actual, is(empty()));
    }

    @Test
    public void canCreateNewLoan() {
        // Given
        Employee employee = new Employee("foo");
        Book book = new Book("foo", 5);
        Loan expected = new Loan(employee, book);
        given(employeeRepository.findOne(employee.getId())).willReturn(employee);
        given(bookRepository.findOne(book.getId())).willReturn(book);
        given(loanService.loanOutBook(employee, book)).willReturn(expected);

        // When
        Loan actual = loanController.create(new LoanCreationDto(employee.getId(), book.getId()));

        // Then
        verify(loanService).loanOutBook(employee, book);
        assertThat(actual, is(expected));
    }

    @Test(expected = NotFoundException.class)
    public void cannotCreateLoanIfEmployeeDoesNotExist() {
        // Given
        Book book = new Book("foo", 3);

        given(bookRepository.findOne(book.getId())).willReturn(book);

        // When
        loanController.create(new LoanCreationDto(999, book.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void cannotCreateLoanIfBookDoesNotExist() {
        // Given
        Employee employee = new Employee("foo");

        given(employeeRepository.findOne(employee.getId())).willReturn(employee);

        // When
        loanController.create(new LoanCreationDto(employee.getId(), 999));
    }

    @Test
    public void canDeleteLoan() {
        // When
        loanController.delete(123L);

        // Then
        verify(loanRepository).delete(123L);
    }

}