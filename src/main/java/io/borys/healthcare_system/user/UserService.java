package io.borys.healthcare_system.user;

import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.SortDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Page<User> findAllDoctors(Pageable pageable) {
        return userRepository.findAllDoctors(pageable);
    }

    Page<User> findAllDoctorsByDoctorSpecialization(DoctorSpecialization specialization, Pageable pageable) {
        return userRepository.findAllDoctorsByDoctorSpecialization(specialization, pageable);
    }

    Page<String> getAllDoctorSpecializations(Pageable pageable) {
        List<String> specializations = new java.util.ArrayList<>(Stream
                .of(DoctorSpecialization.values())
                .map(DoctorSpecialization::name)
                .toList());
        specializations.removeAll(Collections.of("UNSET", "PATIENT"));

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), specializations.size());
        List<String> pageContent = specializations.subList(start, end);
        return new PageImpl<>(pageContent, pageable, specializations.size());
    }
}
