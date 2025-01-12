package io.borys.healthcare_system.auth;

public class BadTokenException extends RuntimeException {
    public BadTokenException(String message) {
        super(message);
    }
}
