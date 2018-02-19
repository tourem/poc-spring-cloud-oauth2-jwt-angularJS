package com.rd.config.client;

import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//import org.springframework.security.oauth2.provider.authentication.TokenExtractor;

@Component
public class JwtHeaderTokenExtractor extends BearerTokenExtractor {


    public String getToken(HttpServletRequest request) {
        return extractToken(request);
    }
}