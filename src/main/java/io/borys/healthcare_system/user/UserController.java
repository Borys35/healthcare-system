package io.borys.healthcare_system.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
