package com.rd.service;

import com.rd.domain.Employee;

import java.util.List;

public interface EmployeeService {

	List<Employee> findAll();

	Employee save(Employee employee);
}