package com.i3.loan.models.dto;

import java.util.Objects;

public class LoanCreationDto {
    private long employeeId;
    private long bookId;

    public LoanCreationDto() {
    }

    public LoanCreationDto(long employeeId, long bookId) {
        this.employeeId = employeeId;
        this.bookId = bookId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public long getBookId() {
        return bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoanCreationDto)) return false;
        LoanCreationDto that = (LoanCreationDto) o;
        return getEmployeeId() == that.getEmployeeId() &&
                getBookId() == that.getBookId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getBookId());
    }
}
