package com.rd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/** Handles logout. Removes access token from token store. **/
@Controller
public class OAuth2Controller {

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping(value = "/api/oauth/revoke-token", method = RequestMethod.GET)
    public ResponseEntity revokeToken(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null) {
            String token = authorizationHeader.replace("Bearer", "").trim();
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            tokenStore.removeAccessToken(oAuth2AccessToken);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
