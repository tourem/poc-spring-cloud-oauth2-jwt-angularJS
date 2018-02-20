package com.rd.config.client;


import com.rd.domain.User;
import feign.Headers;
import feign.RequestLine;

public interface AuthServiceClient {

    @Headers("Content-Type: application/json")
    @RequestLine("POST /oauth/users")
    void createUser(User user);

}