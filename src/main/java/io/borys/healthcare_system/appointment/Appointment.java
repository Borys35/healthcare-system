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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String info;

    private Double price;

    private Integer durationInMinutes;

    private String appointmentType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Builder
    public Appointment(User doctor, User patient, LocalDateTime startDate, String info, Double price, Integer durationInMinutes, String appointmentType, AppointmentStatus status) {
        this.doctor = doctor;
        this.patient = patient;
        this.startDate = startDate;
        this.info = info;
        this.price = price;
        this.durationInMinutes = durationInMinutes;
        this.appointmentType = appointmentType;
        this.endDate = startDate.plusMinutes(durationInMinutes);
        this.status = status;
    }

    public Appointment(User doctor, User patient, LocalDateTime startDate, String info, Double price, Integer durationInMinutes, String appointmentType) {
        this.doctor = doctor;
        this.patient = patient;
        this.startDate = startDate;
        this.info = info;
        this.price = price;
        this.durationInMinutes = durationInMinutes;
        this.appointmentType = appointmentType;
        this.endDate = startDate.plusMinutes(durationInMinutes);
    }
}
