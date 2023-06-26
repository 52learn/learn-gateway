package com.study.gateway.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AppDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppDemoApplication.class, args);
	}

	@Bean
	public MyWebFilter myWebFilter(ObjectMapper objectMapper){
		return new MyWebFilter(objectMapper);
	}

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}

	@RestController
	public static class HelloController{
		@GetMapping("/hello")
		public String hello(){
			return "hello";
		}
		@GetMapping("/prefix/prefixPath")
		public String prefixPath(){
			return "prefixPath";
		}
	}

	@RestController
	public static class UserController{
		@GetMapping("/api/user/getUserInfo")
		public String getUserInfo(){
			return "user info : kim";
		}
	}
}
