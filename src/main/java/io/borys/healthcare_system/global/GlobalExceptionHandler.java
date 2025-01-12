package io.borys.healthcare_system.global;

import io.borys.healthcare_system.appointment.BadRoleException;
import io.borys.healthcare_system.auth.BadTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
    }

    @ExceptionHandler(BadTokenException.class)
    public ResponseEntity<String> badTokenException(BadTokenException e) {
        return ResponseEntity.status(401).body("Bad token error occurred: " + e.getMessage());
    }

    @ExceptionHandler(BadRoleException.class)
    public ResponseEntity<String> badRoleException(BadRoleException e) {
        return ResponseEntity.status(401).body("Bad role error occurred: " + e.getMessage());
    }
}
