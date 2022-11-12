package com.example.sherlockescape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SherlockEscapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SherlockEscapeApplication.class, args);
	}

}
