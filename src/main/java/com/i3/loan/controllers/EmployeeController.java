package com.i3.loan.controllers;

import com.i3.loan.models.Employee;
import com.i3.loan.repositories.EmployeeRepository;
import com.i3.loan.common.exceptions.NotFoundException;
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
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping(path = "/{id}", method = GET)
    public Employee get(@PathVariable long id) {
        Employee employee = employeeRepository.findOne(id);

        if(employee == null) {
            throw new NotFoundException(String.format("No employee with ID: %s", id));
        }

        return employee;
    }

    @RequestMapping(method = GET)
    public List<Employee> get() {
        return employeeRepository.findAll();
    }

    @RequestMapping(method = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    public void delete(@PathVariable long id) {
        employeeRepository.delete(id);
    }
}
