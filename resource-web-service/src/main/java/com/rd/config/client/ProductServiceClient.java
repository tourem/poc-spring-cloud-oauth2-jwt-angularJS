package com.rd.config.client;


import com.rd.domain.ProductResource;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface ProductServiceClient {

    @RequestLine("GET /api/products/{id}")
    ProductResource getProduct(@Param("id") String id);

}