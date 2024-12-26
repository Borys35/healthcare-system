package io.borys.healthcare_system.auth;

import io.borys.healthcare_system.user.LoginUserDto;
import io.borys.healthcare_system.user.RegisterUserDto;
import io.borys.healthcare_system.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(final JwtService jwtService, final AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto dto) {
        User registeredUser = authService.signUp(dto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto dto) {
        User authenticatedUser = authService.authenticate(dto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/test")
    public String test() {
        return "TEST";
    }
}
