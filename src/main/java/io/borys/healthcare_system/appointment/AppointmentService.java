package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.user.User;
import io.borys.healthcare_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public Set<Appointment> findAllByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getRoles().stream().anyMatch(role -> Objects.equals(role.getName(), "DOCTOR")))
            return appointmentRepository.findAllByDoctorId(user.getId());
        else
            return appointmentRepository.findAllByPatientId(user.getId());
    }

    public Appointment save(AppointmentDto appointmentDto) {
        User doctor = userRepository.findById(appointmentDto.doctorId()).orElseThrow();
        if (doctor.getRoles().stream().noneMatch(role -> Objects.equals(role.getName(), "DOCTOR"))) {
            throw new RuntimeException("Doctor role is not doctor");
        }
        User patient = userRepository.findById(appointmentDto.patientId()).orElseThrow();
        if (patient.getRoles().stream().noneMatch(role -> Objects.equals(role.getName(), "PATIENT"))) {
            throw new RuntimeException("Patient role is not patient");
        }
        Appointment appointment = new Appointment(doctor, patient, appointmentDto.appointmentTime(),
                appointmentDto.info(), appointmentDto.price(), appointmentDto.duration(), appointmentDto.specialization());
        return appointmentRepository.save(appointment);
    }
}
