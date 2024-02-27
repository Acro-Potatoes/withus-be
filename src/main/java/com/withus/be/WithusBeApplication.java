package com.withus.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WithusBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithusBeApplication.class, args);
	}

}
