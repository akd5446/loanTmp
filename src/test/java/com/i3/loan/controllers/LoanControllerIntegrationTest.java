package com.i3.loan.controllers;

import com.i3.loan.models.Book;
import com.i3.loan.models.Employee;
import com.i3.loan.models.Loan;
import com.i3.loan.models.dto.LoanCreationDto;
import com.i3.loan.repositories.BookRepository;
import com.i3.loan.repositories.EmployeeRepository;
import com.i3.loan.repositories.LoanRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoanRepository loanRepository;

    private Employee employee1;
    private Employee employee2;

    private Book book1;
    private Book book2;

    @Before
    public void setup() {
        book1 = bookRepository.save(new Book("foo", 5));
        book2 = bookRepository.save(new Book("bar", 3));

        employee1 = employeeRepository.save(new Employee("foo"));
        employee2 = employeeRepository.save(new Employee("bar"));
    }

    @After
    public void cleanup() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void getLoanByIdReturnsNotFoundIfDoesNotExist()
            throws Exception {
        // When
        ResponseEntity actual = testRestTemplate.getForEntity("/loans/1", Loan.class);

        // Then
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getLoanById()
            throws Exception {
        // Given
        Loan expected = new Loan(employee1, book1);
        expected = loanRepository.save(expected);

        // When
        Long id = expected.getId();
        Loan actual = testRestTemplate.getForObject("/loans/" + id, Loan.class);

        // Then
        assertThat(expected, is(actual));
    }

    @Test
    public void getAllLoans()
            throws Exception {
        // Given
        Loan[] expected = {new Loan(employee1, book1), new Loan(employee1, book2), new Loan(employee2, book2)};
        loanRepository.save(Arrays.asList(expected));

        // When
        List<Loan> actual = Arrays.asList(testRestTemplate.getForObject("/loans", Loan[].class));

        // Then
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void createNewLoan()
            throws Exception {
        // Given
        Loan expected = new Loan(employee1, book1);
        assertThat(loanRepository.count(), is(0L));

        LoanCreationDto loanCreation = new LoanCreationDto(employee1.getId(), book1.getId());

        // When
        Loan actual = testRestTemplate.postForObject("/loans", loanCreation, Loan.class);

        // Then
        assertThat(expected.getEmployee(), is(actual.getEmployee()));
        assertThat(expected.getBook(), is(actual.getBook()));

        assertThat(loanRepository.count(), is(1L));
        assertThat(loanRepository.findOne(actual.getId()), is(actual));
    }

    @Test
    public void createNewLoanReturnsNotFoundIfEmployeeNotFound()
            throws Exception {
        // Given
        LoanCreationDto loanCreation = new LoanCreationDto(999, book1.getId());

        // When
        ResponseEntity actual = testRestTemplate.postForEntity("/loans", loanCreation, Loan.class);

        // Then
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void createNewLoanReturnsNotFoundIfBookNotFound()
            throws Exception {
        // Given
        LoanCreationDto loanCreation = new LoanCreationDto(employee1.getId(), 999);

        // When
        ResponseEntity actual = testRestTemplate.postForEntity("/loans", loanCreation, Loan.class);

        // Then
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteLoan()
            throws Exception {
        // Given
        Loan loan = loanRepository.save(new Loan(employee1, book1));
        Long id = loan.getId();

        // When
        testRestTemplate.delete("/loans/" + id);

        // Then
        assertThat(loanRepository.exists(id), is(false));
    }

    @Test
    public void deleteNonExistentLoanDoesNotReturnError()
            throws Exception {
        // When
        testRestTemplate.delete("/loans/1");
    }
}
