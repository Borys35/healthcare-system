package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.auth.LoginResponse;
import io.borys.healthcare_system.role.Role;
import io.borys.healthcare_system.role.RoleRepository;
import io.borys.healthcare_system.user.DoctorAppointmentTypeSet;
import io.borys.healthcare_system.user.LoginUserDto;
import io.borys.healthcare_system.user.RegisterUserDto;
import io.borys.healthcare_system.user.User;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppointmentControllerTest {
    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withExposedPorts(5432)
            .withDatabaseName("healthcare-system")
            .withUsername("borys")
            .withPassword("secret");

    private RestClient client;

    private String doctorJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siaWQiOjIsIm5hbWUiOiJVU0VSIn0seyJpZCI6MywibmFtZSI6IkRPQ1RPUiJ9XSwic3ViIjoiYm9yeXNrYWMxMEBnbWFpbC5tZWQuY29tIiwiaWF0IjoxNzM2NjkxODA3LCJleHAiOjE3MzY2OTU0MDd9.6mhirYhW_OcCUeSPw9eUe7cYj6RMXG0aMCdedk74s8I";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    void init() {
        roleRepository.saveAllAndFlush(
                List.of(
                        new Role("USER"),
                        new Role("ADMIN"),
                        new Role("DOCTOR"),
                        new Role("PATIENT")
                )
        );
    }

    @BeforeEach
    void setUp() {
        client = RestClient.create("http://localhost:" + port);

        jdbcTemplate.execute("""
            TRUNCATE TABLE appointments CASCADE;
            TRUNCATE TABLE user_roles CASCADE;
            TRUNCATE TABLE user_doctor_appointment_types CASCADE;
            TRUNCATE TABLE users RESTART IDENTITY CASCADE;
        """);

        client.post()
                .uri("/auth/register")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(new RegisterUserDto("John", "Doctor", "doe@mail.med.com", "abcd1234"))
                .retrieve()
                .body(User.class);

        client.post()
                .uri("/auth/register")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(new RegisterUserDto("John", "Patient", "doe@mail.com", "abcd1234"))
                .retrieve()
                .body(User.class);


        LoginResponse loginResponse = client.post()
                .uri("/auth/login")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(new LoginUserDto("doe@mail.med.com", "abcd1234"))
                .retrieve()
                .body(LoginResponse.class);

        assert loginResponse != null;
        doctorJwtToken = loginResponse.getToken();

        client.put()
                .uri("/users/doctors/1/specialization")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + doctorJwtToken)
                .body("\"CARDIOLOGIST\"")
                .retrieve()
                .toBodilessEntity();

        DoctorAppointmentTypeSet set = new DoctorAppointmentTypeSet();
        set.setAppointmentTypes(Set.of("Consultation"));
        client.put()
                .uri("/users/doctors/1/appointment-types")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + doctorJwtToken)
                .body(set)
                .retrieve()
                .toBodilessEntity();

        appointmentService.create(
                new AppointmentDto(1L, 2L, LocalDateTime.now(), "TEST INFO", 2000.0, 60, "Consultation", AppointmentStatus.PENDING)
        );

    }

    @Test
    void shouldCreateAppointment() {
        AppointmentDto appointmentDto = new AppointmentDto(
                1L,
                2L,
                LocalDateTime.of(2025, 1, 10, 16, 0, 0),
                "Info",
                1000.0,
                90,
                "Consultation",
                AppointmentStatus.PENDING);

        Appointment appointment = client
                .post()
                .uri("/appointments")
                .header("Authorization", "Bearer " + doctorJwtToken)
                .contentType(APPLICATION_JSON)
                .body(appointmentDto)
                .retrieve()
                .body(Appointment.class);

        assert appointment != null;
        assert appointment.getId() != null;
        assert appointment.getDoctor().getId() == 1;
        assert appointment.getPatient().getId() == 2;
        assert Objects.equals(appointment.getAppointmentType(), "Consultation");
    }

    @Test
    void shouldNotCreateAppointmentDueToInvalidDate() {
        AppointmentDto appointmentDto = new AppointmentDto(
                1L,
                2L,
                LocalDateTime.of(2025, 1, 10, 16, 0, 0),
                "Info",
                1000.0,
                90,
                "Consultation",
                AppointmentStatus.PENDING);

        AppointmentDto appointmentDto2 = new AppointmentDto(
                1L,
                2L,
                LocalDateTime.of(2025, 1, 10, 17, 0, 0),
                "Info",
                1000.0,
                90,
                "Consultation",
                AppointmentStatus.PENDING);

        Appointment appointment = client
                .post()
                .uri("/appointments")
                .header("Authorization", "Bearer " + doctorJwtToken)
                .contentType(APPLICATION_JSON)
                .body(appointmentDto)
                .retrieve()
                .body(Appointment.class);

        assert appointment != null;

        assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            client
                    .post()
                    .uri("/appointments")
                    .header("Authorization", "Bearer " + doctorJwtToken)
                    .contentType(APPLICATION_JSON)
                    .body(appointmentDto2)
                    .retrieve()
                    .body(Appointment.class);
        });
    }

    @Test
    void shouldNotCreateAppointmentDueToInvalidDoctorId() {
        AppointmentDto appointmentDto = new AppointmentDto(
                1L,
                2L,
                LocalDateTime.of(2025, 1, 10, 16, 0, 0),
                "Info",
                1000.0,
                90,
                "Consultation",
                AppointmentStatus.PENDING);

        AppointmentDto appointmentDto2 = new AppointmentDto(
                2L,
                2L,
                LocalDateTime.of(2025, 1, 10, 20, 0, 0),
                "Info",
                1000.0,
                90,
                "Consultation",
                AppointmentStatus.PENDING);

        Appointment appointment = client
                .post()
                .uri("/appointments")
                .header("Authorization", "Bearer " + doctorJwtToken)
                .contentType(APPLICATION_JSON)
                .body(appointmentDto)
                .retrieve()
                .body(Appointment.class);

        assert appointment != null;

        assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            client
                    .post()
                    .uri("/appointments")
                    .header("Authorization", "Bearer " + doctorJwtToken)
                    .contentType(APPLICATION_JSON)
                    .body(appointmentDto2)
                    .retrieve()
                    .body(Appointment.class);
        });
    }

    @Test
    void shouldFindAllAppointmentsForDoctor() {
        List<Appointment> appointments = client
                .get()
                .uri("/appointments/user/1")
                .header("Authorization", "Bearer " + doctorJwtToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assert appointments != null;
        assert appointments.size() == 1;
    }
}
