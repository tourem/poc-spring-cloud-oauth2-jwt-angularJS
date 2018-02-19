package com.rd.config.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mtoure on 19/02/2018.
 */
@Component
public class TokenRequestInterceptor implements RequestInterceptor {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtHeaderTokenExtractor jwtHeaderTokenExtractor;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer "+jwtHeaderTokenExtractor.getToken(request));
    }
}
