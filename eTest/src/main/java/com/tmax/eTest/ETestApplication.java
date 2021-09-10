package com.tmax.eTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
public class ETestApplication {
	@PostConstruct
	public void setTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		SpringApplication.run(ETestApplication.class, args);
	}
}
