package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    private LocalDateTime appointmentTime;

    private String info;

    private Double price;

    private Integer duration;

    private String specialization;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Builder
    public Appointment(User doctor, User patient, LocalDateTime appointmentTime, String info, Double price, Integer duration, String specialization) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.info = info;
        this.price = price;
        this.duration = duration;
        this.specialization = specialization;
    }
}
