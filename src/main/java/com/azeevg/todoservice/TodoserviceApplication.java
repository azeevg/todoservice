package com.azeevg.todoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TodoserviceApplication {


	public static void main(String[] args) {
		SpringApplication.run(TodoserviceApplication.class, args);
	}

}
