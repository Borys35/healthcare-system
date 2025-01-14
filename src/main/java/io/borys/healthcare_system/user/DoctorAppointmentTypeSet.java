package io.borys.healthcare_system.user;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DoctorAppointmentTypeSet {
    private Set<String> appointmentTypes;
}
