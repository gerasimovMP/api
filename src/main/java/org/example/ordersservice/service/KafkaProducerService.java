package org.example.ordersservice.service;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.model.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    @Value("${spring.kafka.topic.order}")
    private String topic;

    public void sendMessage(OrderDTO dto) {
        kafkaTemplate.send(topic, dto);
    }
}