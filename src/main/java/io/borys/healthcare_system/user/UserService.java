package io.borys.healthcare_system.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    List<User> findAllDoctors() {
//        return userRepository.findAllDoctors();
//    }
}
