package fr.larbotech.sample.microservices.productservice;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("#oauth2.hasScope('read') or #oauth2.hasScope('service')")
    public Product find(@PathVariable String id) throws Exception {
        return Product.getMockProduct(id);
    }

    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    //@PreAuthorize("#oauth2.hasScope('read')")
    public Product details(@PathVariable String id) throws Exception {
        return Product.getMockProduct(id);
    }


}
