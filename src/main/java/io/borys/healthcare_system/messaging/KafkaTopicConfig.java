package io.borys.healthcare_system.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic appointmentEvents() {
        return new NewTopic("appointment-events", 1, (short) 1);
    }
}
