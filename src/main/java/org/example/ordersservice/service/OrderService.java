package org.example.ordersservice.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ordersservice.client.FeignOrderClient;
import org.example.ordersservice.model.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    private final FeignOrderClient feignOrderClient;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public OrderService(FeignOrderClient feignOrderClient, KafkaProducerService kafkaProducerService) {
        this.feignOrderClient = feignOrderClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    public OrderDTO createOrderSync(OrderDTO orderDTO) {
        OrderDTO createdOrder = feignOrderClient.createOrder(orderDTO);
        log.info("Order created successfully: {}", createdOrder);
        return createdOrder;
    }

    public void createOrderAsync(OrderDTO orderDTO) {
        kafkaProducerService.sendMessage(orderDTO); // Отправляем в Kafka
        log.info("Order creation request sent to Kafka topic for asynchronous processing.");
    }

    public OrderDTO getOrderById(Long id) {
        OrderDTO order = feignOrderClient.getOrderById(id);
        log.info("Order fetched successfully: {}", order);
        return order;
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        OrderDTO updatedOrder = feignOrderClient.updateOrder(id, orderDTO);
        log.info("Order updated successfully: {}", updatedOrder);
        return updatedOrder;
    }

    public void deleteOrder(Long id) {
        feignOrderClient.deleteOrder(id);
        log.info("Order with ID: {} deleted successfully", id);
    }

    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orders = feignOrderClient.getAllOrders();
        log.info("Fetched {} orders", orders.size());
        return orders;
    }
}
