package com.i3.loan.controllers;

import com.i3.loan.common.exceptions.NotFoundException;
import com.i3.loan.models.Employee;
import com.i3.loan.repositories.EmployeeRepository;
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
public class EmployeeControllerTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void canGetById() {
        // Given
        Employee expected = new Employee("foo");
        given(employeeRepository.findOne(123L)).willReturn(expected);

        // When
        Employee actual = employeeController.get(123L);

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NotFoundException.class)
    public void throwsNotFoundWhenRetrievingNonExistentEmployee() {
        // When
        employeeController.get(123L);
    }

    @Test
    public void canGetListOfEmployees() {
        // Given
        Employee[] expected = {new Employee("foo"), new Employee("bar"), new Employee("baz")};
        given(employeeRepository.findAll()).willReturn(Arrays.asList(expected));

        // When
        List<Employee> actual = employeeController.get();

        // Then
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void getListIsEmptyIfNoEmployeesExist() {
        // When
        List<Employee> actual = employeeController.get();

        // Then
        assertThat(actual, is(empty()));
    }

    @Test
    public void canCreateNewEmployee() {
        // Given
        Employee expected = new Employee("foo");
        given(employeeRepository.save(expected)).willReturn(expected);

        // When
        Employee actual = employeeController.create(expected);

        // Then
        verify(employeeRepository).save(expected);
        assertThat(actual, is(expected));
    }

    @Test
    public void canDeleteEmployee() {
        // When
        employeeController.delete(123L);

        // Then
        verify(employeeRepository).delete(123L);
    }

}