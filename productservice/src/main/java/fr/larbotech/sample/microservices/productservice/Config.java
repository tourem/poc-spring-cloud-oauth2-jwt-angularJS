package fr.larbotech.sample.microservices.productservice;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

   // @LoadBalanced
   // @Bean
   // public RestTemplate detailsServiceRestTemplate() {
   //     return new RestTemplate();
   // }

   @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

}