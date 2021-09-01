package com.tmax.eTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ETestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ETestApplication.class, args);
	}
}
