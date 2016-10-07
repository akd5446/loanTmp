package com.i3.loan.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Entity
public class Loan {
    public static final Period DEFAULT_LOAN_DURATION = Period.ofMonths(3);

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Book book;

    private LocalDate loanDate;

    private LocalDate returnDate;

    public Loan(Employee employee, Book book) {
        this.employee = employee;
        this.book = book;
        this.loanDate = LocalDate.now();
        this.returnDate = this.loanDate.plus(DEFAULT_LOAN_DURATION);
    }

    public Loan() {
    }

    public long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return getId() == loan.getId() &&
                Objects.equals(getEmployee(), loan.getEmployee()) &&
                Objects.equals(getBook(), loan.getBook()) &&
                Objects.equals(getLoanDate(), loan.getLoanDate()) &&
                Objects.equals(getReturnDate(), loan.getReturnDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmployee(), getBook(), getLoanDate(), getReturnDate());
    }
}
