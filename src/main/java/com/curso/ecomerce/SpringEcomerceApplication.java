package com.curso.ecomerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringEcomerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEcomerceApplication.class, args);
	}

}
