package org.example.ordersservice.client;

import org.example.ordersservice.model.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "notificationService", url = "http://localhost:8081")
public interface FeignOrderClient {

    @PostMapping("/api/orders")
    OrderDTO createOrder(@RequestBody OrderDTO orderDTO);


    @GetMapping("/api/orders/{id}")
    OrderDTO getOrderById(@PathVariable("id") Long id);


    @PutMapping("/api/orders/{id}")
    OrderDTO updateOrder(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO);


    @DeleteMapping("/api/orders/{id}")
    void deleteOrder(@PathVariable("id") Long id);

    @GetMapping("/api/orders")
    List<OrderDTO> getAllOrders();
}
