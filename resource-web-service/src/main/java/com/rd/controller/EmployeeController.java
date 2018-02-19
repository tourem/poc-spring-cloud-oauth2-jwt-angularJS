package com.rd.controller;

import com.rd.config.JwtTokenExtractor;
import com.rd.config.client.AuthServiceClient;
import com.rd.config.client.JwtHeaderTokenExtractor;
import com.rd.config.client.ProductServiceClient;
import com.rd.config.client.TokenRequestInterceptor;
import com.rd.domain.Employee;
import com.rd.domain.ProductResource;
import com.rd.domain.User;
import com.rd.repository.AuthorityRepository;
import com.rd.service.EmployeeService;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @Value("${product.host.service}")
    private String productService;


   // @Autowired
   // private DiscoveryClient discoveryClient;

   // @Autowired
   // private LoadBalancerClient loadBalancerClient;

    @Autowired
    private TokenRequestInterceptor interceptor;

    @Autowired
    private JwtHeaderTokenExtractor jwtHeaderTokenExtractor;

    //@Autowired
    //private AuthServiceClient authClient;

    // @Autowired
    // private ProductServiceClient productServiceClient;

    // @Autowired
    // private com.netflix.discovery.DiscoveryClient netflixDiscoveryClient;

    @Autowired
    private JwtTokenExtractor jwtTokenExtractor;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Employee> saveEmployee(@RequestBody @Valid Employee employee,
                                                 BindingResult result, HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();

        String token = jwtHeaderTokenExtractor.getToken(request);

        ResponseEntity<ProductResource> responseRemote = details("12", token);

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

    public ResponseEntity<ProductResource> details(String id, String token) {

      /*  ServiceInstance serviceInstance;
        serviceInstance = discoveryClient.getInstances("products-service")
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("products-service not found"));

        ServiceInstance serviceInstanceLoadBalancer =
                loadBalancerClient.choose("products-service");

        String url = serviceInstanceLoadBalancer.getUri().toString() + "/api/products/"+id;

        HttpEntity<?> entity = jwtTokenExtractor.getRequest(request); */


        // url = netflixDiscoveryClient.getNextServerFromEureka("details-service", false).getHomePageUrl()+"/details/"+id;

        ProductServiceClient api = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(interceptor)
               // .target(ProductServiceClient.class, "http://localhost:8091");
                .target(ProductServiceClient.class, productService);
        // return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
       // ProductResource productResource = api.getProduct(id, token);
        ProductResource productResource = api.getProduct(id);
        return ResponseEntity.ok(productResource);
        //return restTemplate.getForEntity(url, String.class);
    }


    public void create(String login) {
        User user = new User();
        user.setUsername(login);
        user.setEmail("ibra@gao.ml");
        user.setActivated(true);
        user.setPassword("ibra");
       // user.setAuthorities(new HashSet<>(authorityRepository.findAll()));
       // authClient.createUser(user);
    }
}
