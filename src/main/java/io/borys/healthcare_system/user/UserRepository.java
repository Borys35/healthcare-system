package io.borys.healthcare_system.user;

import io.borys.healthcare_system.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // List<User> findAllDoctors();
}
