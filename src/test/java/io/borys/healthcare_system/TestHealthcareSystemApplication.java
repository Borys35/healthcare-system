package io.borys.healthcare_system;

import org.springframework.boot.SpringApplication;

public class TestHealthcareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(HealthcareSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
