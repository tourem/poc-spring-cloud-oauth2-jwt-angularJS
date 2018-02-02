package com.rd.config.switchuser;

import org.springframework.security.oauth2.provider.OAuth2Request;

/**
 * Created by mtoure on 30/01/2018.
 */
public class SwitchUserUtils {

    public static OAuth2Request toOAuth2Request(OAuth2Request other) {
        return new OAuth2Request(other.getRequestParameters(), other.getClientId(), other.getAuthorities(), other.isApproved(), other
                .getScope(), other.getResourceIds(), other.getRedirectUri(), other.getResponseTypes(), other
                .getExtensions());
    }
}
