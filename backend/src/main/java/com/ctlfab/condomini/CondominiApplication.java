package com.ctlfab.condomini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ctlfab.condomini")
public class CondominiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CondominiApplication.class, args);
	}
}
