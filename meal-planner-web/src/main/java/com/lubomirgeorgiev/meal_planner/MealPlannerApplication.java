package com.lubomirgeorgiev.meal_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class MealPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealPlannerApplication.class, args);
	}

}
