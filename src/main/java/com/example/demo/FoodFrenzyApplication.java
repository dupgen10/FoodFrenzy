package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring Security is enabled — do NOT exclude SecurityAutoConfiguration
@SpringBootApplication
public class FoodFrenzyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodFrenzyApplication.class, args);
	}

}
