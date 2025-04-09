package org.example.ordersservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.ordersservice.model.OrderDTO;
import org.example.ordersservice.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrderController {

    private final KafkaProducerService kafkaProducer;

    @Autowired
    public OrderController(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping
    @Operation(summary = "Send order to Kafka")
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        kafkaProducer.sendMessage(orderDTO);
        return new ResponseEntity<>("Order sent to Kafka", HttpStatus.CREATED);
    }
}