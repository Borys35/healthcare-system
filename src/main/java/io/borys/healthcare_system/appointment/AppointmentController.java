package io.borys.healthcare_system.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Set<Appointment> getAppointments(@PathVariable Long id) {
        return appointmentService.findAllByUser(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Appointment createAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.create(appointmentDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR')")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody AppointmentDto appointmentDto) {
        return appointmentService.update(id, appointmentDto);
    }
}
