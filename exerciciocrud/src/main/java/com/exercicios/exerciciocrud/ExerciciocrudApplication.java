package com.exercicios.exerciciocrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ExerciciocrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExerciciocrudApplication.class, args);
	}

}
