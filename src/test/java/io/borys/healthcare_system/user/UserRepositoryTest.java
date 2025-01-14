package io.borys.healthcare_system.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data
        User patient1 = new User("Alice", "Johnson", "alice.johnson@example.com", "patient1pass123");
        User doctor1 = new User("Dr. Bob", "Williams", "bob.williams@example.med.com", "doctor1pass123");
        doctor1.setDoctorSpecialization(DoctorSpecialization.CARDIOLOGIST);
        doctor1.setDoctorAppointmentTypes(Set.of("Consultation"));

        User doctor2 = new User("Dr. Carol", "Davis", "carol.davis@example.med.com", "doctor2pass123");
        doctor2.setDoctorSpecialization(DoctorSpecialization.NEUROLOGIST);
        doctor2.setDoctorAppointmentTypes(Set.of("Consultation", "MRI Review"));

        when(userRepository.findByEmail("bob.williams@example.med.com"))
                .thenReturn(Optional.of(doctor1));

        when(userRepository.findAllDoctors(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(doctor1, doctor2)));

        when(userRepository.findAllDoctorsByDoctorSpecialization(eq(DoctorSpecialization.CARDIOLOGIST), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(doctor1)));
    }

    @Test
    void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("bob.williams@example.med.com");

        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("bob.williams@example.med.com");

        verify(userRepository, times(1)).findByEmail("bob.williams@example.med.com");
    }

    @Test
    void testFindAllDoctors() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> doctors = userRepository.findAllDoctors(pageable);

        assertThat(doctors.getTotalElements()).isEqualTo(2);
        assertThat(doctors.getContent())
                .extracting(User::getDoctorSpecialization)
                .doesNotContain(DoctorSpecialization.PATIENT);

        verify(userRepository, times(1)).findAllDoctors(pageable);
    }

    @Test
    void testFindAllDoctorsBySpecialization() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> cardiologists = userRepository.findAllDoctorsByDoctorSpecialization(DoctorSpecialization.CARDIOLOGIST, pageable);

        assertThat(cardiologists.getTotalElements()).isEqualTo(1);
        assertThat(cardiologists.getContent().getFirst().getDoctorSpecialization()).isEqualTo(DoctorSpecialization.CARDIOLOGIST);

        verify(userRepository, times(1)).findAllDoctorsByDoctorSpecialization(DoctorSpecialization.CARDIOLOGIST, pageable);
    }
}
