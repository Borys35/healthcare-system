package io.borys.healthcare_system.appointment;

public class NoAppointmentTypeFoundException extends RuntimeException {
    public NoAppointmentTypeFoundException(String message) {
        super(message);
    }
}
