package io.borys.healthcare_system.appointment;

import io.borys.healthcare_system.auth.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AppointmentTests {
    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    private RestClient client;

    private final String doctorJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siaWQiOjIsIm5hbWUiOiJVU0VSIn0seyJpZCI6MywibmFtZSI6IkRPQ1RPUiJ9XSwic3ViIjoiYm9yeXNrYWMxMEBnbWFpbC5tZWQuY29tIn0.lRQ9DQrOdpYiPQcv2ct8KQuz4VyaMhXGuA3tgQXeHfw";

    @BeforeEach
    void setUp() {
        client = RestClient.create("http://localhost:" + port);
    }

    @Test
    void shouldFindAllAppointmentsForDoctor() {
        Appointment appointment = client
                .get()
                .uri("/appointments/user/1")
                .header("Authorization", "Bearer " + doctorJwtToken)
                .retrieve()
                .body(Appointment.class);

        assert appointment != null;
        assert appointment.getDoctor().getId() == 1;
        assert appointment.getPatient().getId() == 2;
    }

    @Test
    void shouldFindOneAppointment() {
    }
}
