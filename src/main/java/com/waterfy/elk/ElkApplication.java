package com.waterfy.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ElkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElkApplication.class, args);
	}

}
