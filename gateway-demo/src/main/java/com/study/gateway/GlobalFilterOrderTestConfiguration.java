package com.study.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GlobalFilterOrderTestConfiguration {

    @Bean
    @Order(98)
    public GlobalFilter firstGlobalFilter(){
        return (exchange, chain) -> {
            log.info("first global filter  pre ...");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("first global filter  post ...");
            }));
        };
    }
    @Bean
    @Order(99)
    public GlobalFilter secondGlobalFilter(){
        return (exchange, chain) -> {
            log.info("second global filter  pre ...");
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("second global filter  post ...");
            }));
        };
    }

}
