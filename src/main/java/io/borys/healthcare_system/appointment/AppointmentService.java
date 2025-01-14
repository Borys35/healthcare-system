package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.global.DoctorPatientHelper;
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
    private final DoctorPatientHelper doctorPatientHelper;

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

    /*
    This method creates a new appointment.
    It checks if doctor and patient are valid,
    if the time doesn't collide with other appointments,
    or if the doctor has given appointmentType as their service.
    Available appointmentType values can be set
    by a Doctor using [PUT] /users/doctor/{id}/appointment-types
     */
    public Appointment create(AppointmentDto appointmentDto) {
        User doctor = doctorPatientHelper.checkAndReturnDoctor(appointmentDto.doctorId());
        User patient = doctorPatientHelper.checkAndReturnPatient(appointmentDto.patientId());
        LocalDateTime start = appointmentDto.date();
        LocalDateTime end = appointmentDto.date().plusMinutes(appointmentDto.durationInMinutes());
        boolean isBetween = appointmentRepository.existsByDoctorIdAndDates(appointmentDto.doctorId(), start, end);
        if (isBetween) {
            throw new NotAvailableDateException("Not available date: " + appointmentDto.date() + " with duration: " + appointmentDto.durationInMinutes());
        }
        if (!doctorPatientHelper.checkIfDoctorHasAppointmentType(appointmentDto.doctorId(), appointmentDto.appointmentType())) {
            throw new NoAppointmentTypeFoundException("No appointment type found for doctor: " + appointmentDto.appointmentType());
        }
        Appointment appointment = new Appointment(doctor, patient, appointmentDto.date(),
                appointmentDto.info(), appointmentDto.price(), appointmentDto.durationInMinutes(), appointmentDto.appointmentType());
        return appointmentRepository.save(appointment);
    }

    public Appointment update(Long id, AppointmentDto appointmentDto) {
        User doctor = doctorPatientHelper.checkAndReturnDoctor(appointmentDto.doctorId());
        User patient = doctorPatientHelper.checkAndReturnPatient(appointmentDto.patientId());
        LocalDateTime start = appointmentDto.date();
        LocalDateTime end = appointmentDto.date().plusMinutes(appointmentDto.durationInMinutes());
        boolean isBetween = appointmentRepository.existsByDoctorIdAndDates(appointmentDto.doctorId(), start, end);
        if (isBetween) {
            throw new NotAvailableDateException("Not available date: " + appointmentDto.date() + " with duration: " + appointmentDto.durationInMinutes());
        }
        if (!doctorPatientHelper.checkIfDoctorHasAppointmentType(appointmentDto.doctorId(), appointmentDto.appointmentType())) {
            throw new NoAppointmentTypeFoundException("No appointment type found for doctor: " + appointmentDto.appointmentType());
        }
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartDate(start);
        appointment.setInfo(appointmentDto.info());
        appointment.setPrice(appointmentDto.price());
        appointment.setDurationInMinutes(appointmentDto.durationInMinutes());
        appointment.setAppointmentType(appointmentDto.appointmentType());
        appointmentRepository.save(appointment);

        return appointment;
    }
}
