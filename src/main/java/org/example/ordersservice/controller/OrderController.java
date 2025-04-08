package org.example.ordersservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.ordersservice.model.OrderDTO;
import org.example.ordersservice.model.OrderEntity;
import org.example.ordersservice.service.OrderService;
import org.example.ordersservice.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    // Получить все заказы
    @GetMapping
    @Operation(summary = "Get filters by telegramId") // изменить нейминги
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderEntity> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orderMapper.toDtoList(orders);
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    // Получить заказ по ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Optional<OrderEntity> orderOptional = orderService.getOrderById(id);
        return orderOptional.map(order -> new ResponseEntity<>(orderMapper.toDto(order), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Создать новый заказ
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderEntity orderEntity = orderMapper.toEntity(orderDTO);
        OrderEntity createdOrder = orderService.createOrder(orderEntity);
        OrderDTO createdOrderDTO = orderMapper.toDto(createdOrder);
        return new ResponseEntity<>(createdOrderDTO, HttpStatus.CREATED);
    }

    // Обновить заказ
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
        Optional<OrderEntity> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            OrderEntity orderEntity = orderMapper.toEntity(orderDTO);
            orderEntity.setId(id);  // Устанавливаем существующий ID для обновления
            OrderEntity updatedOrder = orderService.updateOrder(orderEntity);
            return new ResponseEntity<>(orderMapper.toDto(updatedOrder), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Удалить заказ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Optional<OrderEntity> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
