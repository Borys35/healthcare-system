package io.borys.healthcare_system.appointment;

public class BadRoleException extends RuntimeException {
    public BadRoleException(String message) {
        super(message);
    }
}
