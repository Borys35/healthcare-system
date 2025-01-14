package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.user.User;
import io.borys.healthcare_system.user.DoctorSpecialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppointmentRepositoryTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private Appointment appointment1;
    private Appointment appointment2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User data
        User doctor = new User("Dr. John", "Doe", "john.doe@example.com", "password123");
        doctor.setDoctorSpecialization(DoctorSpecialization.CARDIOLOGIST);

        User patient1 = new User("Jane", "Smith", "jane.smith@example.com", "password123");
        User patient2 = new User("Alice", "Johnson", "alice.johnson@example.com", "password123");

        // Mock Appointment data
        appointment1 = new Appointment(
                doctor,
                patient1,
                LocalDateTime.of(2025, 1, 14, 10, 0),
                "Consultation",
                100.0,
                60,
                "Consultation"
        );

        appointment2 = new Appointment(
                doctor,
                patient2,
                LocalDateTime.of(2025, 1, 15, 14, 0),
                "Follow-up",
                150.0,
                45,
                "Follow-up"
        );

        // Mock repository methods
        when(appointmentRepository.findAllByDoctorId(doctor.getId()))
                .thenReturn(Set.of(appointment1, appointment2));

        when(appointmentRepository.findAllByPatientId(patient1.getId()))
                .thenReturn(Set.of(appointment1));

        when(appointmentRepository.existsByDoctorIdAndStartDateIsBetweenOrEndDateIsBetween(
                eq(doctor.getId()), any(), any(), any(), any()))
                .thenReturn(true);

        when(appointmentRepository.existsByDoctorIdAndOccupiedDate(
                eq(doctor.getId()), any(), any()))
                .thenReturn(true);

        when(appointmentRepository.existsByDoctorIdAndNotAppointmentIdAndOccupiedDate(
                eq(appointment2.getId()), eq(doctor.getId()), any(), any()))
                .thenReturn(false);
    }

    @Test
    void testFindAllByDoctorId() {
        Set<Appointment> appointments = appointmentRepository.findAllByDoctorId(appointment1.getDoctor().getId());

        assertThat(appointments).hasSize(2);
        assertThat(appointments).contains(appointment1, appointment2);

        verify(appointmentRepository, times(1)).findAllByDoctorId(appointment1.getDoctor().getId());
    }

    @Test
    void testFindAllByPatientId() {
        Set<Appointment> appointments = appointmentRepository.findAllByPatientId(appointment1.getPatient().getId());

        assertThat(appointments).hasSize(1);
        assertThat(appointments).contains(appointment1);

        verify(appointmentRepository, times(1)).findAllByPatientId(appointment1.getPatient().getId());
    }

    @Test
    void testExistsByDoctorIdAndStartDateIsBetweenOrEndDateIsBetween() {
        boolean exists = appointmentRepository.existsByDoctorIdAndStartDateIsBetweenOrEndDateIsBetween(
                appointment1.getDoctor().getId(),
                LocalDateTime.of(2025, 1, 14, 9, 0),
                LocalDateTime.of(2025, 1, 14, 11, 0),
                LocalDateTime.of(2025, 1, 14, 9, 0),
                LocalDateTime.of(2025, 1, 14, 11, 0)
        );

        assertThat(exists).isTrue();

        verify(appointmentRepository, times(1)).existsByDoctorIdAndStartDateIsBetweenOrEndDateIsBetween(
                eq(appointment1.getDoctor().getId()), any(), any(), any(), any());
    }

    @Test
    void testExistsByDoctorIdAndOccupiedDate() {
        boolean exists = appointmentRepository.existsByDoctorIdAndOccupiedDate(
                appointment1.getDoctor().getId(),
                LocalDateTime.of(2025, 1, 14, 9, 0),
                LocalDateTime.of(2025, 1, 14, 11, 0)
        );

        assertThat(exists).isTrue();

        verify(appointmentRepository, times(1)).existsByDoctorIdAndOccupiedDate(
                eq(appointment1.getDoctor().getId()), any(), any());
    }

    @Test
    void testExistsByDoctorIdAndNotAppointmentIdAndOccupiedDate() {
        boolean exists = appointmentRepository.existsByDoctorIdAndNotAppointmentIdAndOccupiedDate(
                appointment2.getId(),
                appointment2.getDoctor().getId(),
                LocalDateTime.of(2025, 1, 15, 13, 0),
                LocalDateTime.of(2025, 1, 15, 15, 0)
        );

        assertThat(exists).isFalse();

        verify(appointmentRepository, times(1)).existsByDoctorIdAndNotAppointmentIdAndOccupiedDate(
                eq(appointment2.getId()), eq(appointment2.getDoctor().getId()), any(), any());
    }
}
