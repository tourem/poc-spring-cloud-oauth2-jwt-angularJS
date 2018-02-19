package com.rd.config.client.feign;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.util.Assert;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    /**
     * The logger instance used by this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);

    /**
     * The authorization header name.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * The {@code Bearer} token type.
     */
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     * Current OAuth2 authentication context.
     */
    private final OAuth2ClientContext oauth2ClientContext;

    /**
     * Creates new instance of {@link OAuth2FeignRequestInterceptor} with client context.
     *
     * @param oauth2ClientContext the OAuth2 client context
     */
    public OAuth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
        Assert.notNull(oauth2ClientContext, "Context can not be null");
        this.oauth2ClientContext = oauth2ClientContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(RequestTemplate template) {

        if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
            LOGGER.warn("The Authorization token has been already set");
        } else if (oauth2ClientContext.getAccessTokenRequest().getExistingToken() == null) {
            LOGGER.warn("Can not obtain existing token for request, if it is a non secured request, ignore.");
        } else {
            LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE,
                    oauth2ClientContext.getAccessTokenRequest().getExistingToken().toString()));
        }
    }
}