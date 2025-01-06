package io.borys.healthcare_system;

import io.borys.healthcare_system.role.Role;
import io.borys.healthcare_system.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class HealthcareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcareSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner start(RoleRepository roleRepository) {
		return args -> {
			roleRepository.saveAll(List.of(new Role("ADMIN"), new Role("USER"), new Role("DOCTOR"), new Role("PATIENT")));
		};
	}

	@GetMapping
	public String index() {
		return "Welcome to Healthcare System!";
	}
}
