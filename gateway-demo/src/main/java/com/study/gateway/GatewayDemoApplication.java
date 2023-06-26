package com.study.gateway;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class GatewayDemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(GatewayDemoApplication.class, args);
	}
	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}


	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder){
		return builder.routes()
			.route("modifyRequestBody",r->r.order(Integer.MAX_VALUE)
				.header("modify-request-body")
				.filters(f->f.modifyRequestBody(String.class, Map.class, new RewriteFunction<String, Map>() {
					@Override
					public Publisher<Map> apply(ServerWebExchange exchange, String s) {
						try{
							Map<Object,Object> requestBodyMap = JSONObject.parseObject(s, Map.class);
							requestBodyMap.putIfAbsent("timestamp",System.currentTimeMillis());
							return Mono.just(requestBodyMap);
						}catch (Exception e){
							log.error(" Error parse request body: {}",s);
						}
						return Mono.just(Collections.emptyMap());
					}
				}))
				.uri("http://127.0.0.1:8080")
			)
			.route("modifyResponseBody",r->r.order(Integer.MAX_VALUE-2)
				.header("modify-response-body")
				.filters(f->f.modifyResponseBody(String.class, Map.class, new RewriteFunction<String, Map>() {
					@Override
					public Publisher<Map> apply(ServerWebExchange exchange, String s) {
						log.info("response content: {}",s);
						Map<String,Object> map = new HashMap<>();
						map.put("timestamp",System.currentTimeMillis());
						return Mono.just(map);
					}
				}))
				.uri("http://127.0.0.1:8080")
			)
			.route("addRequestParameter",r->r.header("add-request-parameter")
				.filters(f->f.addRequestParameter("addColor","green"))
				.uri("http://127.0.0.1:8080")
			)
			.build();
	}

	@Bean
	public GlobalFilter customerFilter(){
		return new CustomerGlobalFilter();
	}



}
