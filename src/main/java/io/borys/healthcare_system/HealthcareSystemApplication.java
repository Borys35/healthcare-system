package io.borys.healthcare_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class HealthcareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcareSystemApplication.class, args);
	}

	@GetMapping
	public String index() {
		return "Welcome to Healthcare System!";
	}
}
