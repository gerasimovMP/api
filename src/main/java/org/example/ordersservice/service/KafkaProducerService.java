package org.example.ordersservice.service;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.model.OrderDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private static final String TOPIC = "order_topic";

    public void sendMessage(OrderDTO dto) {
        kafkaTemplate.send(TOPIC, dto);
    }
}