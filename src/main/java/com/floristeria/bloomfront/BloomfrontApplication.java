package com.floristeria.bloomfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BloomfrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloomfrontApplication.class, args);
	}

}