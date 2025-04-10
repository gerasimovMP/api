package org.example.ordersservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.ordersservice.model.OrderDTO;
import org.example.ordersservice.service.OrderService;
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

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Синхронный эндпоинт
    @PostMapping("/sync")
    @Operation(summary = "Create order synchronously")
    public ResponseEntity<String> createOrderSync(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrderSync(orderDTO);

        return new ResponseEntity<>("Order created successfully: " + createdOrder, HttpStatus.OK);
    }

    // Асинхронный эндпоинт
    @PostMapping("/async")
    @Operation(summary = "Create order asynchronously")
    public ResponseEntity<String> createOrderAsync(@Valid @RequestBody OrderDTO orderDTO) {
        orderService.createOrderAsync(orderDTO);

        return new ResponseEntity<>("Order is being processed asynchronously", HttpStatus.OK);
    }
}