package com.rd.config.client;


import com.rd.domain.User;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by imrenagi on 5/8/17.
 */
//@FeignClient(decode404 = true) si le service est voi, le empty est vue comme erreor 404
//@FeignClient(name = "auth-service", url = "http://auth.docker.localhost")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/oauth/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    void createUser(User user);

}