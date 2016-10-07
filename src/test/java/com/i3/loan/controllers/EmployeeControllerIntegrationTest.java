package com.i3.loan.controllers;

import com.i3.loan.models.Employee;
import com.i3.loan.repositories.EmployeeRepository;
import org.junit.After;
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
public class EmployeeControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @After
    public void cleanup() {
        employeeRepository.deleteAll();
    }

    @Test
    public void getEmployeeByIdReturnsNotFoundIfDoesNotExist()
            throws Exception {
        // When
        ResponseEntity actual = testRestTemplate.getForEntity("/employees/1", Employee.class);

        // Then
        assertThat(actual.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getEmployeeById()
            throws Exception {
        // Given
        Employee expected = new Employee("foo");
        expected = employeeRepository.save(expected);

        // When
        Long id = expected.getId();
        Employee actual = testRestTemplate.getForObject("/employees/" + id, Employee.class);

        // Then
        assertThat(expected, is(actual));
    }

    @Test
    public void getAllEmployees()
            throws Exception {
        // Given
        Employee[] expected = {new Employee("foo"), new Employee("bar"), new Employee("baz")};
        employeeRepository.save(Arrays.asList(expected));

        // When
        List<Employee> actual = Arrays.asList(testRestTemplate.getForObject("/employees", Employee[].class));

        // Then
        assertThat(actual, containsInAnyOrder(expected));
    }

    @Test
    public void createNewEmployee()
            throws Exception {
        // Given
        Employee expected = new Employee("foo");
        assertThat(employeeRepository.count(), is(0L));

        // When
        Employee actual = testRestTemplate.postForObject("/employees", expected, Employee.class);

        // Then
        assertThat(expected.getName(), is(actual.getName()));

        assertThat(employeeRepository.count(), is(1L));
        assertThat(employeeRepository.findOne(actual.getId()), is(actual));
    }

    @Test
    public void deleteEmployee()
            throws Exception {
        // Given
        Employee employee = employeeRepository.save(new Employee("foo"));
        Long id = employee.getId();

        // When
        testRestTemplate.delete("/employees/" + id);

        // Then
        assertThat(employeeRepository.exists(id), is(false));
    }

    @Test
    public void deleteNonExistentEmployeeDoesNotReturnError()
            throws Exception {
        // When
        testRestTemplate.delete("/employees/1");
    }
}
