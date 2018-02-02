package com.rd.service;

import com.rd.config.switchuser.SwitchUserAuthenticationToken;
import com.rd.config.switchuser.SwitchUserUtils;
import com.rd.domain.User;
import com.rd.repository.UserRepository;
import com.rd.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class SwitchUserAuthenticationTokenService {

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthorizationServerTokenServices tokenService;

    @Autowired
    TokenEndpoint tokenEndpoint;

    @Autowired
    private ClientDetailsService clientDetailsService;


    public OAuth2AccessToken createImpersonationAccessToken(String login) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<GrantedAuthority> impersonationAuthorities = new ArrayList<>(authorities);
        OAuth2Authentication source = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        // add current user authentication (to switch back from impersonation):
        SwitchUserGrantedAuthority switchUserAuthority =
                new SwitchUserGrantedAuthority(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR, source);
        impersonationAuthorities.add(switchUserAuthority);
        UserDetails newUserDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(login)
                        .authorities(impersonationAuthorities)
                        .password(userDetails.getPassword())
                        .build();
        Authentication userPasswordAuthentiation =
                new UsernamePasswordAuthenticationToken(newUserDetails, null, impersonationAuthorities);

        Map<String, String> parameters = new HashMap<>();
        //ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
        OAuth2Request oauthRequest = SwitchUserUtils.toOAuth2Request(source.getOAuth2Request());
        OAuth2Authentication authentication = new OAuth2Authentication(oauthRequest, userPasswordAuthentiation);
        OAuth2AccessToken createAccessToken = tokenService.createAccessToken(authentication);
        return createAccessToken;
    }


    @Transactional
    public OAuth2AccessToken createAccessToken(String username) {

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsernameCaseInsensitive(username);

        return defaultTokenServices.createAccessToken(createAuthentication(user, oAuth2Authentication));
    }

    @Transactional
    public OAuth2AccessToken createAccessTokenSwitchUser(String username) {

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsernameCaseInsensitive(username);

        return defaultTokenServices.createAccessToken(createAuthentication(user, oAuth2Authentication));
    }


    private static OAuth2Authentication createAuthentication(User user, OAuth2Authentication oAuth2Authentication) {
        return new OAuth2Authentication(
                SwitchUserUtils.toOAuth2Request(oAuth2Authentication.getOAuth2Request()),
                new SwitchUserAuthenticationToken(user.getUsername(), user.getAuthorities()));
    }
}
