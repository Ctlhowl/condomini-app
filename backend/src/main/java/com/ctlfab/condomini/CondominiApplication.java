package com.ctlfab.condomini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.ctlfab.condomini")
@EnableScheduling
public class CondominiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CondominiApplication.class, args);
	}
}
