package io.borys.healthcare_system.appointment;

public class NotAvailableDateException extends RuntimeException {
    public NotAvailableDateException(String message) {
        super(message);
    }
}
