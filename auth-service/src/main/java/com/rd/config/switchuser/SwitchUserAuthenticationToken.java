package com.rd.config.switchuser;

import com.rd.domain.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SwitchUserAuthenticationToken extends AbstractAuthenticationToken {

    private String principal;

    public SwitchUserAuthenticationToken(String username, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.principal = username;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
