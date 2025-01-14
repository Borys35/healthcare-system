package io.borys.healthcare_system.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Page<User> findAllDoctors(Pageable pageable) {
        return userRepository.findAllDoctors(pageable);
    }
}
