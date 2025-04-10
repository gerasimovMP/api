package org.example.ordersservice.client;

import org.example.ordersservice.model.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationService", url = "http://localhost:8081")
public interface FeignOrderClient {

    // Синхронный запрос
    @PostMapping("/api/orders")
    OrderDTO createOrder(@RequestBody OrderDTO orderDTO);  // Синхронный метод для отправки заказа

/*    // Асинхронный запрос
    @PostMapping("/api/orders")
    CompletableFuture<OrderDTO> createOrderAsync(@RequestBody OrderDTO orderDTO);  // Асинхронный метод для отправки заказа*/
}