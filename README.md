# Healthcare System API

A monolithic REST API for managing healthcare operations, built using Spring Boot and Java. This application facilitates interactions between doctors and patients, including secure role-based authentication, profile management, and appointment handling.

## Features

### General
- Secure role-based JWT authentication for patients and doctors.
- RESTful APIs for CRUD operations.
- Kafka integration for event-driven communication.
- PostgreSQL database for structured and efficient data management.

### Doctor Features
- Create and manage profiles.
- List specialization and appointment services.
- Manage appointments for patients.

### Patient Features
- View all registered doctors.
- Access detailed information about doctors, including specialization and appointment services.

## Technologies Used

- **Backend**: Java, Spring Boot (REST API, Spring Security, Spring Data JPA).
- **Database**: PostgreSQL.
- **Messaging**: Apache Kafka.
- **Testing**: JUnit, Mockito, Testcontainers.
- **Build Tool**: Maven/Gradle.
- **Deployment**: Docker.

## System Architecture

- Monolithic architecture.
- PostgreSQL for persistent storage.
- Kafka for event-driven processing.
- REST APIs for client-server interaction.

## Setup and Installation

### Prerequisites
- Java 17 or later
- Maven or Gradle
- Docker (for running PostgreSQL, Kafka, and Zookeeper)

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/healthcare-system-api.git
   cd healthcare-system-api
   ```

2. Configure the application properties:
   Update the `application.properties` file:
   ```properties
   spring.application.name=healthcare-system
   spring.datasource.url=jdbc:postgresql://localhost:5432/healthcare-system
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   spring.datasource.generate-unique-name=false
   spring.datasource.name=healthcare-system
   security.jwt.secret-key=dummy-secret-key
   security.jwt.expiration-time=3600000
   server.servlet.session.cookie.http-only=true
   server.servlet.session.cookie.secure=true
   server.servlet.session.cookie.name=MY_SESS_COOKIE
   spring.sql.init.mode=always
   spring.jpa.hibernate.ddl-auto=update
   spring.kafka.bootstrap-servers=localhost:9092
   spring.kafka.consumer.group-id=healthcare-consumers
   spring.kafka.consumer.auto-offset-reset=earliest
   spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
   spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
   ```

3. Configure Docker Compose:
   Create a `compose.yaml` file with the following content:
   ```yaml
   services:
     postgres:
       image: 'postgres:latest'
       container_name: postgres
       environment:
         - 'POSTGRES_DB=healthcare-system'
         - 'POSTGRES_PASSWORD=secret'
         - 'POSTGRES_USER=borys'
       ports:
         - '5432:5432'
     zookeeper:
       image: 'confluentinc/cp-zookeeper:latest'
       container_name: 'zookeeper'
       environment:
         ZOOKEEPER_CLIENT_PORT: 2181
         ZOOKEEPER_TICK_TIME: 2000
       ports:
         - '2181:2181'
     kafka:
       image: 'confluentinc/cp-kafka:latest'
       container_name: 'kafka'
       depends_on:
         - zookeeper
       ports:
         - '9092:9092'
         - '29092:29092'
       environment:
         KAFKA_BROKER_ID: 1
         KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
         KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_HOST://localhost:29092
         KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
         KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
         KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
   ```

4. Start the Docker containers:
   ```bash
   docker-compose -f compose.yaml up -d
   ```

5. Build the application:
   ```bash
   ./mvnw clean package
   ```

6. Run the application:
   ```bash
   java -jar target/healthcare-system-api.jar
   ```

7. Access the API documentation:
   Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) in your browser.

### Running Tests

Run unit and integration tests:
```bash
./mvnw test
```

## Testing

- **Unit Tests**: Written using JUnit and Mockito to test individual components.
- **Integration Tests**: Testcontainers is used to create isolated PostgreSQL and Kafka environments for reliable testing.

## Challenges and Solutions

### Secure Authentication
- Implemented Spring Security with JWT to ensure secure role-based access control.

### Event-Driven Communication
- Integrated Kafka for asynchronous workflows, configuring producers and consumers for real-time updates.

### Reliable Testing
- Used Testcontainers to simulate production-like environments for PostgreSQL during tests.

## Future Improvements
- Add pagination and filtering for API responses.
- Implement user notifications using Kafka and WebSocket.
- Introduce a front-end client for easier interaction with the API.

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes and push the branch.
4. Submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For any inquiries or feedback, please reach out to me at [your-email@example.com].
