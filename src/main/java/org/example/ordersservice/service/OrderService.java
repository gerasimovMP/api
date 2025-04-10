package org.example.ordersservice.service;

import org.example.ordersservice.client.FeignOrderClient;
import org.example.ordersservice.model.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final FeignOrderClient feignOrderClient;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public OrderService(FeignOrderClient feignOrderClient, KafkaProducerService kafkaProducerService) {
        this.feignOrderClient = feignOrderClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    public OrderDTO createOrderSync(OrderDTO orderDTO) {
        return feignOrderClient.createOrder(orderDTO);
    }

    public void createOrderAsync(OrderDTO orderDTO) {
        kafkaProducerService.sendMessage(orderDTO);
    }

    public OrderDTO getOrderById(Long id) {
        return feignOrderClient.getOrderById(id);
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        return feignOrderClient.updateOrder(id, orderDTO);
    }

    public void deleteOrder(Long id) {
        feignOrderClient.deleteOrder(id);
    }

    public List<OrderDTO> getAllOrders() {
        return feignOrderClient.getAllOrders();
    }
}
