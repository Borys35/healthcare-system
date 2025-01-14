package io.borys.healthcare_system.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    public Set<Appointment> findAllByDoctorId(Long id);
    public Set<Appointment> findAllByPatientId(Long id);

    public boolean existsByDoctorIdAndStartDateIsBetweenOrEndDateIsBetween(Long doctor_id, LocalDateTime startDate, LocalDateTime startDate2, LocalDateTime endDate, LocalDateTime endDate2);

    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
    FROM Appointment a
    WHERE a.doctor.id = :doctorId
      AND (a.startDate BETWEEN :startDate AND :endDate
           OR a.endDate BETWEEN :startDate AND :endDate)
    """)
    public boolean existsByDoctorIdAndOccupiedDate(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
    FROM Appointment a
    WHERE a.doctor.id = :doctorId
      AND a.id != :appointmentId
      AND (a.startDate BETWEEN :startDate AND :endDate
           OR a.endDate BETWEEN :startDate AND :endDate)
    """)
    public boolean existsByDoctorIdAndNotAppointmentIdAndOccupiedDate(
            @Param("appointmentId") Long appointmentId,
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
