package com.rd.config;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    private HttpEntity<?> getRequest(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getRequestToken(request));
        return new HttpEntity<>(null, headers);
    }

    private String getRequestToken(HttpServletRequest request) {
        Authentication token = new BearerTokenExtractor().extract(request);
        if (token != null) {
            return (String) token.getPrincipal();
        }
        throw new SecurityException();
    }

}
