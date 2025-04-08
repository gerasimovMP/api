package org.example.ordersservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedMessage(String orderId) {
        kafkaTemplate.send("order-topic", "Order Created: " + orderId);
    }

    public void sendOrderUpdatedMessage(String orderId) {
        kafkaTemplate.send("order-topic", "Order Updated: " + orderId);
    }
}