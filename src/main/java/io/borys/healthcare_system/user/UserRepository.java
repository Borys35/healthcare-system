package io.borys.healthcare_system.user;

import io.borys.healthcare_system.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u
        WHERE u.doctorSpecialization IS NOT NULL
        AND u.doctorSpecialization != 'PATIENT'
""")
    Page<User> findAllDoctors(Pageable pageable);

    @Query("""
        SELECT u FROM User u
        WHERE u.doctorSpecialization IS NOT NULL
        AND u.doctorSpecialization != 'PATIENT'
        AND u.doctorSpecialization = :doctorSpecialization
""")
    Page<User> findAllDoctorsByDoctorSpecialization(DoctorSpecialization doctorSpecialization, Pageable pageable);
}
