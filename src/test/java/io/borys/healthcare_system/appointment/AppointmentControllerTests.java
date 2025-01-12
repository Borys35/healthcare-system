package io.borys.healthcare_system.appointment;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppointmentControllerTests {
    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    private RestClient client;

    private final String doctorJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siaWQiOjIsIm5hbWUiOiJVU0VSIn0seyJpZCI6MywibmFtZSI6IkRPQ1RPUiJ9XSwic3ViIjoiYm9yeXNrYWMxMEBnbWFpbC5tZWQuY29tIiwiaWF0IjoxNzM2NjkxODA3LCJleHAiOjE3MzY2OTU0MDd9.6mhirYhW_OcCUeSPw9eUe7cYj6RMXG0aMCdedk74s8I";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void init() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS appointments");
    }

    @BeforeEach
    void setUp() {
        client = RestClient.create("http://localhost:" + port);
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
                "Specialization");

        Appointment appointment = client
                .post()
                .uri("/appointments")
                .header("Authorization", "Bearer " + doctorJwtToken)
                .contentType(APPLICATION_JSON)
                .body(appointmentDto)
                .retrieve()
                .body(Appointment.class);

        assert appointment != null;
        log.info(appointment.toString());
        assert appointment.getId() != null;
        assert appointment.getDoctor().getId() != 1;
        assert appointment.getPatient().getId() != 2;
        assert Objects.equals(appointment.getSpecialization(), "Specialization");
    }

    @Test
    void shouldNotCreateAppointment() {

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
        log.info(appointments.toString());
        assert appointments.size() == 1;
    }

    @Test
    void shouldFindOneAppointment() {
    }
}
