package org.example.ordersservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

/*    @KafkaListener(topics = "order-topic", groupId = "order-service-consumer-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // Логика обработки сообщения
    }*/
}
