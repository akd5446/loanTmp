package com.i3.loan.repositories;

import com.i3.loan.models.Book;
import com.i3.loan.models.Employee;
import com.i3.loan.models.Loan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanRepositoryIntegrationTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoanRepository loanRepository;

    private Book book1;
    private Book book2;
    private Employee employee1;
    private Employee employee2;

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
    public void countByBook() throws Exception {
        // Given
        loanRepository.save(new Loan(employee1, book1));
        loanRepository.save(new Loan(employee2, book1));
        loanRepository.save(new Loan(employee1, book2));

        // When
        long actual = loanRepository.countByBook(book1);

        // Then
        assertThat(actual, is(2L));
    }

    @Test
    public void findOverdue() throws Exception {
        // Given
        Loan overdue1 = new Loan(employee1, book1);
        overdue1.setReturnDate(LocalDate.now().minusDays(2));
        overdue1 = loanRepository.save(overdue1);

        Loan overdue2 = new Loan(employee1, book2);
        overdue2.setReturnDate(LocalDate.now().minusWeeks(3));
        overdue2 = loanRepository.save(overdue2);

        Loan notOverdue = new Loan(employee2, book1);
        notOverdue.setReturnDate(LocalDate.now().plusDays(1));
        notOverdue = loanRepository.save(notOverdue);

        // When
        List<Loan> actual = loanRepository.findOverdue();

        // Then
        assertThat(actual, contains(overdue1, overdue2));
    }

}