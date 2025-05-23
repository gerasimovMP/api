package org.example.ordersservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.CreateOrderRequest;
import org.example.notificationservice.CreateOrderResponse;
import org.example.ordersservice.client.NotificationSoapClient;
import org.example.ordersservice.model.OrderDTO;
import org.example.ordersservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders")
public class OrderController {

    private final OrderService orderService;
    private final NotificationSoapClient soapClient;

    @Autowired
    public OrderController(OrderService orderService, NotificationSoapClient soapClient) {
        this.orderService = orderService;
        this.soapClient = soapClient;
    }

    @PostMapping
    @Operation(summary = "Create order synchronously")
    public ResponseEntity<String> createOrderSync(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("Received POST request to create order sync: {}", orderDTO);
        OrderDTO createdOrder = orderService.createOrderSync(orderDTO);
        return new ResponseEntity<>("Order created successfully: " + createdOrder, HttpStatus.OK);
    }


    @PostMapping("/async")
    @Operation(summary = "Create order asynchronously")
    public ResponseEntity<String> createOrderAsync(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("Received POST request to create order async, message sent to Kafka");
        orderService.createOrderAsync(orderDTO);
        return new ResponseEntity<>("Order is being processed asynchronously", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        log.info("Received GET request for order with ID: {}", id);
        OrderDTO order = orderService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Received GET request to fetch all orders");
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
        log.info("Received PUT request to update order with ID: {} with new data: {}", id, orderDTO);
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        log.info("Received DELETE request to remove order with ID: {}", id);
        orderService.deleteOrder(id);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/soap")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) throws DatatypeConfigurationException {
        CreateOrderRequest request = new CreateOrderRequest();
        if (orderDTO.getId() != null) {
            request.setId(orderDTO.getId());
        }
        request.setItemId(orderDTO.getItemId());
        request.setQuantity(orderDTO.getQuantity());
        if (orderDTO.getOrderDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String formattedDate = orderDTO.getOrderDate().format(formatter);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(formattedDate);
            request.setOrderDate(xmlGregorianCalendar);
        }
        request.setStatus(orderDTO.getStatus());
        CreateOrderResponse response = soapClient.sendOrder(request);
        return ResponseEntity.ok("Order sent via SOAP. Status: " + response.getStatus());
    }
}