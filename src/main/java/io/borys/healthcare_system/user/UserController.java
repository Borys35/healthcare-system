package io.borys.healthcare_system.user;

import io.borys.healthcare_system.global.DoctorPatientHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/doctors/{id}/specialization")
    void setSpecialization(@PathVariable long id, @RequestBody DoctorSpecialization specialization) {
        User doctor = doctorPatientHelper.checkAndReturnDoctor(id);
        doctor.setDoctorSpecialization(specialization);
        userRepository.save(doctor);
    }

    @PutMapping("/doctors/{id}/appointment-types")
    void setAppointmentTypes(@PathVariable long id, @RequestBody DoctorAppointmentTypeSet doctorAppointmentTypeSet) {
        User doctor = doctorPatientHelper.checkAndReturnDoctor(id);
        doctor.setDoctorAppointmentTypes(doctorAppointmentTypeSet.getAppointmentTypes());
        userRepository.save(doctor);
    }

    @GetMapping("/doctors")
    Page<User> findAllDoctors(@RequestParam(required = false) DoctorSpecialization specialization, Pageable pageable) {
        System.out.println("SPEC " + specialization);
        if (specialization != null) {
            System.out.println("NOT NULL");
            return userRepository.findAllDoctorsByDoctorSpecialization(specialization, pageable);
        } else {
            System.out.println("NULL");
            return userRepository.findAllDoctors(Pageable.unpaged());
        }
    }
}
