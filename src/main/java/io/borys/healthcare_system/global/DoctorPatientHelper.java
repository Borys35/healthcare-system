package io.borys.healthcare_system.global;

import io.borys.healthcare_system.appointment.BadRoleException;
import io.borys.healthcare_system.user.User;
import io.borys.healthcare_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/*
This is helper component which mainly contains checking methods
for Doctors and Patients (both are User class).
 */
@Component
@RequiredArgsConstructor
public class DoctorPatientHelper {
    private final UserRepository userRepository;

    public User checkAndReturnDoctor(Long doctorId) {
        User doctor = userRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        if (doctor.getRoles().stream().noneMatch(role -> Objects.equals(role.getName(), "DOCTOR"))) {
            throw new BadRoleException("Doctor role is not doctor");
        }
        return doctor;
    }

    public User checkAndReturnPatient(Long patientId) {
        User patient = userRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        if (patient.getRoles().stream().noneMatch(role -> Objects.equals(role.getName(), "PATIENT"))) {
            throw new BadRoleException("Patient role is not patient");
        }
        return patient;
    }

    public boolean checkIfDoctorHasAppointmentType(Long doctorId, Set<String> types) {
        User doctor = userRepository.findById(doctorId).orElseThrow();
        return doctor.getDoctorAppointmentTypes().containsAll(types);
    }

    public boolean checkIfDoctorHasAppointmentType(Long doctorId, String type) {
        User doctor = userRepository.findById(doctorId).orElseThrow();
        return doctor.getDoctorAppointmentTypes().contains(type);
    }
}
