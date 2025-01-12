package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.user.User;
import io.borys.healthcare_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id).orElseThrow();
    }

    private User checkAndReturnDoctor(Long doctorId) {
        User doctor = userRepository.findById(doctorId).orElseThrow();
        if (doctor.getRoles().stream().noneMatch(role -> Objects.equals(role.getName(), "DOCTOR"))) {
            throw new BadRoleException("Doctor role is not doctor");
        }
        return doctor;
    }

    private User checkAndReturnPatient(Long patientId) {
        User patient = userRepository.findById(patientId).orElseThrow();
        if (patient.getRoles().stream().noneMatch(role -> Objects.equals(role.getName(), "PATIENT"))) {
            throw new BadRoleException("Patient role is not patient");
        }
        return patient;
    }

    public Appointment create(AppointmentDto appointmentDto) {
        User doctor = checkAndReturnDoctor(appointmentDto.doctorId());
        User patient = checkAndReturnPatient(appointmentDto.patientId());
        LocalDateTime start = appointmentDto.date();
        LocalDateTime end = appointmentDto.date().plusMinutes(appointmentDto.durationInMinutes());
        boolean isBetween = appointmentRepository.existsByDoctorIdAndDates(appointmentDto.doctorId(), start, end);
        if (isBetween) {
            throw new NotAvailableDateException("Not available date: " + appointmentDto.date() + " with duration: " + appointmentDto.durationInMinutes());
        }
        Appointment appointment = new Appointment(doctor, patient, appointmentDto.date(),
                appointmentDto.info(), appointmentDto.price(), appointmentDto.durationInMinutes(), appointmentDto.specialization());
        return appointmentRepository.save(appointment);
    }

    public Appointment update(Long id, AppointmentDto appointmentDto) {
        User doctor = checkAndReturnDoctor(appointmentDto.doctorId());
        User patient = checkAndReturnPatient(appointmentDto.patientId());
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartDate(appointmentDto.date());
        appointment.setInfo(appointmentDto.info());
        appointment.setPrice(appointmentDto.price());
        appointment.setDurationInMinutes(appointmentDto.durationInMinutes());
        appointment.setSpecialization(appointmentDto.specialization());
        appointmentRepository.save(appointment);

        return appointment;
    }
}
