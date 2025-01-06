package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.user.User;
import io.borys.healthcare_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Set<Appointment> getAppointments(@PathVariable Long id) {
        return appointmentService.findAllByUser(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Appointment saveAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.save(appointmentDto);
    }

//    @PutMapping
//    @PreAuthorize("hasAnyRole('ROLE_DOCTOR')")
//    public Appointment changeAppointmentStatus() {
//        return appointmentService.save(appointmentDto);
//    }
}
