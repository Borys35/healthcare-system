package io.borys.healthcare_system.appointment;

import java.time.LocalDateTime;

public record AppointmentDto(Long doctorId,
                             Long patientId,
                             LocalDateTime appointmentTime,
                             String info,
                             Double price,
                             Integer durationInMinutes,
                             String specialization) {
}
