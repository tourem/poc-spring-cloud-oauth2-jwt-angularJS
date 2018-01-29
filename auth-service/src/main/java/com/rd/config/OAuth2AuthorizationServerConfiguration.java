package com.rd.config;


import com.rd.security.Authorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter implements EnvironmentAware {

    private static final String ENV_OAUTH = "authentication.oauth.";
    private static final String PROP_CLIENTID = "clientid";
    private static final String PROP_SECRET = "secret";
    private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";

    private RelaxedPropertyResolver propertyResolver;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    // TODO externalize token related data to configuration, store clients in DB
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       clients.jdbc(dataSource); //clients in data base

       /* clients.inMemory()
                .withClient(propertyResolver.getProperty(PROP_CLIENTID))
                .scopes("read", "write")
                .authorities(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name())
                .secret(propertyResolver.getProperty(PROP_SECRET))
                .accessTokenValiditySeconds(propertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS, Integer.class, 1800))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .and()
                .withClient("other-client")
                .scopes("read")
                .authorities(Authorities.ROLE_USER.name())
                .secret(propertyResolver.getProperty(PROP_SECRET))
                .accessTokenValiditySeconds(propertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS, Integer.class, 1800))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token"); */

    }


    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
    }

    /*
     * The endpoints can only be accessed by a not logged in user or a user with
     * the specified role
     */
    // TODO externalise configuration
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED')")
                .checkTokenAccess("hasAuthority('ROLE_TRUSTED')");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    // TODO encrypt password
    @Bean
    protected JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new CustomTokenEnhancer();
        converter.setKeyPair(
                new KeyStoreKeyFactory(new ClassPathResource("pspeLocal.jks"), "admin123".toCharArray()).getKeyPair("pspeLocal"));
        return converter;
    }

    /*
     * Add custom user principal information to the JWT token
     */
    // TODO additional information fields should be get from configuration
    protected static class CustomTokenEnhancer extends JwtAccessTokenConverter {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            //User user = (User) authentication.getPrincipal();

            Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

            DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);

            // Get the authorities from the user
            Set<GrantedAuthority> authoritiesSet = new HashSet<>(authentication.getAuthorities());

            // Generate String array
            String[] authorities = new String[authoritiesSet.size()];

            int i = 0;
            for (GrantedAuthority authority : authoritiesSet)
                authorities[i++] = authority.getAuthority();

            info.put("authorities", authorities);
            customAccessToken.setAdditionalInformation(info);

            return super.enhance(customAccessToken, authentication);
        }
    }

}