package com.resumeradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ResumeRadarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeRadarApplication.class, args);
	}

}
