package io.borys.healthcare_system.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "appointment-events", groupId = "healthcare-consumers")
    public void consumeAppointmentEvent(String message) {
        System.out.println("Received message: " + message);
        // Further process
    }
}
