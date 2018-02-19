package com.rd.config.client;


import com.rd.domain.ProductResource;
import com.rd.domain.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by imrenagi on 5/8/17.
 */
//@FeignClient(decode404 = true) si le service est voi, le empty est vue comme erreor 404
//@FeignClient(name = "products-service", url = "http://product.docker.localhost")
//@FeignClient(name = "products-service")
@Headers("Content-Type: application/json")
public interface ProductServiceClient {

   // @Headers("Host:product.docker.localhost")
    @RequestLine("GET /api/products/{id}")
    //@Headers("Authorization: Bearer {token}")
    //ProductResource getProduct(@Param("id")  String id, @Param("token")  String token);
    ProductResource getProduct(@Param("id")  String id);

}