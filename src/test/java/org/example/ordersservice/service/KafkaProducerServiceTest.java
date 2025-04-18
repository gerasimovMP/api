package org.example.ordersservice.service;

import org.example.ordersservice.model.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    private final String testTopic = "orders-topic";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaProducerService, "topic", testTopic);
    }

    @Test
    @DisplayName("order sent to orders-topic")
    void sendMessage_ShouldUseCorrectTopic() {
        OrderDTO testOrder = new OrderDTO(3L, "item-123", 3, null, "COMPLETED");
        kafkaProducerService.sendMessage(testOrder);
        verify(kafkaTemplate).send(testTopic, testOrder);
    }
}