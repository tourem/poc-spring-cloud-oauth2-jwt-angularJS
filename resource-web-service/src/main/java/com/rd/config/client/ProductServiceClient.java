package com.rd.config.client;


import com.rd.domain.ProductResource;
import com.rd.domain.User;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by imrenagi on 5/8/17.
 */
//@FeignClient(decode404 = true) si le service est voi, le empty est vue comme erreor 404
@FeignClient(name = "products-service")
public interface ProductServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/products/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ProductResource getProduct(@PathVariable("id")  String id);

}