package io.borys.healthcare_system.user;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}
