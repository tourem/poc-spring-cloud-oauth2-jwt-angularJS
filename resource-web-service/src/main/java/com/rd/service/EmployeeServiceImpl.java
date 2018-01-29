package com.rd.service;


import com.rd.domain.Employee;
import com.rd.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> findAll() {
		List<Employee> results = employeeRepository.findAll();
		return results;
	}

	@Override
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}
}