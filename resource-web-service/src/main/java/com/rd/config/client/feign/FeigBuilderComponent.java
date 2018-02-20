package com.rd.config.client.feign;

import feign.Feign;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FeigBuilderComponent {
    @Autowired
    @Qualifier("userTokenRequestInterceptor")
    private RequestInterceptor userTokenRequestInterceptor;

    @Autowired
    @Qualifier("serviceTokenRequestInterceptor")
    private RequestInterceptor serviceTokenRequestInterceptor;

    public <T> T targetUser(Class<T> apiType, String url) {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(userTokenRequestInterceptor)
                .target(apiType, url);
    }

    public <T> T targetService(Class<T> apiType, String url) {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(serviceTokenRequestInterceptor)
                .target(apiType, url);
    }
}
