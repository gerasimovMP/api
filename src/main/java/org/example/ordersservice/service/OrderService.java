package org.example.ordersservice.service;

import org.example.ordersservice.client.FeignOrderClient;
import org.example.ordersservice.model.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return feignOrderClient.createOrder(orderDTO);  // Синхронный вызов
    }

    public void createOrderAsync(OrderDTO orderDTO) {
        kafkaProducerService.sendMessage(orderDTO);
    }


/*    // Асинхронный метод для создания заказа
    public void createOrderAsync(OrderDTO orderDTO) {
        CompletableFuture<OrderDTO> futureOrder = feignOrderClient.createOrderAsync(orderDTO);  // Асинхронный вызов
        futureOrder.thenAccept(order -> {
            System.out.println("Order created asynchronously: " + order);
        });
    }*/
}