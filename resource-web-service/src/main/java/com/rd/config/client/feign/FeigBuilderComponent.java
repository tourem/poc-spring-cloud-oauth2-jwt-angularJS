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
    @Qualifier("tokenRequestInterceptor")
    private RequestInterceptor requestInterceptor;

    public <T> T target(Class<T> apiType, String url) {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(requestInterceptor)
                .target(apiType, url);
    }

}
