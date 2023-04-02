package com.neoris.santiago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NeorisChallengeApplication {

	public static final String pass = "admin";
	public static void main(String[] args) {
		SpringApplication.run(NeorisChallengeApplication.class, args);
	}

}
