package com.Projects.SmartRailApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmartRailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartRailApplication.class, args);
	}

}
