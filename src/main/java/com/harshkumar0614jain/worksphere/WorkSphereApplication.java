package com.harshkumar0614jain.worksphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class WorkSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkSphereApplication.class, args);
	}

}
