package com.rd.controller;

import com.rd.domain.User;
import com.rd.repository.AuthorityRepository;
import com.rd.repository.UserRepository;
import com.rd.service.TokenProvider;
import com.rd.service.SwitchUserAuthenticationTokenService;
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
    SwitchUserAuthenticationTokenService switchUserAuthenticationTokenService;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
        OAuth2AccessToken oAuth2AccessToken = switchUserAuthenticationTokenService.createAccessToken("admin");
        OAuth2AccessToken oAuth2AccessToken2 = switchUserAuthenticationTokenService.createImpersonationAccessToken("admin");
       String token = oAuth2AccessToken.getValue();
       String token2 = oAuth2AccessToken2.getValue();
        userService.create(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/oauth/switch_user", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> switchUser(HttpServletRequest request) {
        return ResponseEntity.ok(tokenProvider.createToken(request.getParameter("username"), request.getParameter("client_id")));
    }
}
