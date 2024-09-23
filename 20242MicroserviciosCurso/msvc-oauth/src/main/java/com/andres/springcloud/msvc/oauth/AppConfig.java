package com.andres.springcloud.msvc.oauth;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
// import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    WebClient webClient(WebClient.Builder builder,
            ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        return builder
        .baseUrl("http://msvc-users")
        .filter(lbFunction)
        .build();
    }

    // @Bean
    // @LoadBalanced
    // WebClient.Builder webClient() {
    //     return WebClient.builder().baseUrl("http://msvc-users");
    // }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
