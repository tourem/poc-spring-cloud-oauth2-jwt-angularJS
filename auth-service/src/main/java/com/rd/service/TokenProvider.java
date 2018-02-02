package com.rd.service;

import com.rd.domain.User;
import com.rd.repository.OauthClientDetailsRepository;
import com.rd.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "authorities";
    private static final String NAME_KEY = "user_name";

    @Autowired
    private OauthClientDetailsRepository oauthClientDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    private KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("pspeLocal.jks"), "admin123".toCharArray()).getKeyPair("pspeLocal");

    @Transactional
    public String createToken(String username, String clientId) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsernameCaseInsensitive(username);

        Integer accessTokenValidityInMs = oauthClientDetailsRepository.findByClientId(clientId).getAccess_token_validity();

        long now = (new Date()).getTime();
        Date validity = new Date(now + accessTokenValidityInMs);

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(oAuth2Authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(NAME_KEY, user.getUsername())
                //.claim("scope", Lists.asList(new String[]("read", "write")))
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .setExpiration(validity)
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString())
                .compact();
    }

    public Authentication getAuthentication(final String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }

    public Claims getClaims(final String token) {
        return Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .parseClaimsJws(token)
                .getBody();
    }
}
