package io.borys.healthcare_system.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    public Set<Appointment> findAllByDoctorId(Long id);
    public Set<Appointment> findAllByPatientId(Long id);
}
