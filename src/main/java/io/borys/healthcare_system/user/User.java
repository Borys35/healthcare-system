package io.borys.healthcare_system.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.borys.healthcare_system.appointment.Appointment;
import io.borys.healthcare_system.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    @JsonIgnore
    private String email;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private String password;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(updatable = false)
    private Date joinedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    @ToString.Exclude
    @JsonIgnore
    private Set<Appointment> patientAppointments = new HashSet<>();

    // Doctor-specified fields
    @OneToMany(mappedBy = "doctor")
    @ToString.Exclude
    @JsonIgnore
    private Set<Appointment> doctorAppointments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private DoctorSpecialization doctorSpecialization = DoctorSpecialization.UNSET;

    @ElementCollection
    private Set<String> doctorAppointmentTypes = new HashSet<>();

    @Builder
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
