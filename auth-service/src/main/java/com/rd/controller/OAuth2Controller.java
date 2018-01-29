package com.rd.controller;

import com.google.common.collect.Sets;
import com.rd.domain.User;
import com.rd.repository.AuthorityRepository;
import com.rd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/** Handles logout. Removes access token from token store. **/
//@RequestMapping("/oauth")
@Controller
public class OAuth2Controller {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.GET)
    public ResponseEntity revokeToken(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null) {
            String token = authorizationHeader.replace("Bearer", "").trim();
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            tokenStore.removeAccessToken(oAuth2AccessToken);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('service')")
    @RequestMapping(value = "/oauth/users", method = RequestMethod.POST)
    public ResponseEntity createUser(@Valid @RequestBody User user) {
       /* User account = new User();
        account.setUsername(login);
        account.setEmail("ibra@gao.ml");
        account.setActivated(true);
        account.setPassword("ibra");
        account.setAuthorities(Sets.newHashSet(authorityRepository.findAll()));*/
        userService.create(user);
        return new ResponseEntity(HttpStatus.OK);
    }
}
