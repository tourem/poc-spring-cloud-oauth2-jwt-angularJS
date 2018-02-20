package com.rd.controller;

import com.rd.config.client.AuthServiceClient;
import com.rd.config.client.ProductServiceClient;
import com.rd.config.client.feign.FeigBuilderComponent;
import com.rd.domain.Employee;
import com.rd.domain.ProductResource;
import com.rd.domain.User;
import com.rd.repository.AuthorityRepository;
import com.rd.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeController {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    private EmployeeService employeeService;

    @Value("${product.service.url}")
    private String productService;

    @Value("${auth.service.url}")
    private String authService;

    @Autowired
    private FeigBuilderComponent feigBuilderComponent;

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Employee> saveEmployee(@RequestBody @Valid Employee employee,
                                                 BindingResult result, HttpServletRequest request) {


        ResponseEntity<ProductResource> responseRemote = details("12");

        create("ibra");

        create(employee.getFirstName());
        if (!result.hasErrors()) {
            //employee.setIdEmployee(Long.parseLong(responseRemote.getBody().));
            employee.setFirstName(responseRemote.getBody().getDetails());
            employeeService.save(employee);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<ProductResource> details(String id) {
        ProductServiceClient api = feigBuilderComponent.targetUser(ProductServiceClient.class, productService);
        ProductResource productResource = api.getProduct(id);
        return ResponseEntity.ok(productResource);
    }


    public void create(String login) {
        User user = new User();
        user.setUsername(login);
        user.setEmail("ibra@gao.ml");
        user.setActivated(true);
        user.setPassword("ibra");
        user.setAuthorities(new HashSet<>(authorityRepository.findAll()));
        AuthServiceClient api = feigBuilderComponent.targetService(AuthServiceClient.class, authService);
        api.createUser(user);
    }
}
