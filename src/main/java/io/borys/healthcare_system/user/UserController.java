package io.borys.healthcare_system.user;

import io.borys.healthcare_system.global.DoctorPatientHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final DoctorPatientHelper doctorPatientHelper;

    public UserController(UserRepository userRepository, DoctorPatientHelper doctorPatientHelper) {
        this.userRepository = userRepository;
        this.doctorPatientHelper = doctorPatientHelper;
    }

    @GetMapping("")
    List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    User findById(@PathVariable long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @PostMapping("")
    void save(@RequestBody User user) {
        userRepository.save(user);
    }

    @PutMapping("/doctor/{id}/specialization")
    void setSpecialization(@PathVariable long id, @RequestBody DoctorSpecialization specialization) {
        User doctor = doctorPatientHelper.checkAndReturnDoctor(id);
        doctor.setDoctorSpecialization(specialization);
        userRepository.save(doctor);
    }

    @PutMapping("/doctor/{id}/appointment-types")
    void setAppointmentTypes(@PathVariable long id, @RequestBody DoctorAppointmentTypeSet doctorAppointmentTypeSet) {
        User doctor = doctorPatientHelper.checkAndReturnDoctor(id);
        doctor.setDoctorAppointmentTypes(doctorAppointmentTypeSet.getAppointmentTypes());
        userRepository.save(doctor);
    }
}
