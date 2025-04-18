package org.example.ordersservice.service;

import org.example.ordersservice.client.FeignOrderClient;
import org.example.ordersservice.model.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private FeignOrderClient feignOrderClient;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("order create succes")
    void createOrderSync_ShouldReturnCreatedOrder() {
        OrderDTO inputOrder = new OrderDTO();
        inputOrder.setId(1L);

        OrderDTO mockResponse = new OrderDTO();
        mockResponse.setId(1L);
        mockResponse.setStatus("CREATED");

        when(feignOrderClient.createOrder(inputOrder)).thenReturn(mockResponse);
        OrderDTO result = orderService.createOrderSync(inputOrder);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CREATED", result.getStatus());
        verify(feignOrderClient).createOrder(inputOrder);
    }

    @Test
    @DisplayName("order sent to kafka")
    void createOrderAsync_ShouldSendMessageToKafka() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);

        doNothing().when(kafkaProducerService).sendMessage(orderDTO);

        assertDoesNotThrow(() -> orderService.createOrderAsync(orderDTO));
        verify(kafkaProducerService).sendMessage(orderDTO);
    }


    @Test
    @DisplayName("order exists")
    void getOrderById_ShouldReturnOrder() {
        Long orderId = 1L;
        OrderDTO mockOrder = new OrderDTO();
        mockOrder.setId(orderId);

        when(feignOrderClient.getOrderById(orderId)).thenReturn(mockOrder);
        OrderDTO result = orderService.getOrderById(orderId);

        assertEquals(orderId, result.getId());
        verify(feignOrderClient).getOrderById(orderId);
    }

    @Test
    @DisplayName("order dont exists")
    void getOrderById_ShouldThrowException_WhenFeignFails() {
        Long orderId = 999L;
        when(feignOrderClient.getOrderById(orderId))
                .thenThrow(new RuntimeException("Feign Client Error"));
        assertThrows(RuntimeException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    @DisplayName("order update succes")
    void updateOrder_ShouldReturnUpdatedOrder() {
        Long orderId = 1L;
        OrderDTO inputOrder = new OrderDTO();
        inputOrder.setStatus("UPDATED");

        OrderDTO mockResponse = new OrderDTO();
        mockResponse.setId(orderId);
        mockResponse.setStatus("UPDATED");

        when(feignOrderClient.updateOrder(orderId, inputOrder)).thenReturn(mockResponse);
        OrderDTO result = orderService.updateOrder(orderId, inputOrder);

        assertEquals("UPDATED", result.getStatus());
        verify(feignOrderClient).updateOrder(orderId, inputOrder);
    }

    @Test
    @DisplayName("order deleted succes")
    void deleteOrder_ShouldCallFeignClient() {
        Long orderId = 1L;
        doNothing().when(feignOrderClient).deleteOrder(orderId);

        assertDoesNotThrow(() -> orderService.deleteOrder(orderId));
        verify(feignOrderClient).deleteOrder(orderId);
    }

    @Test
    @DisplayName("get orders succes")
    void getAllOrders_ShouldReturnOrderList() {
        OrderDTO testOrder = new OrderDTO(
                1L,
                "item-123",
                2,
                LocalDateTime.parse("2023-01-01T10:00:00"),
                "PENDING"
        );

        when(feignOrderClient.getAllOrders()).thenReturn(List.of(testOrder));
        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        OrderDTO returnedOrder = result.get(0);
        assertEquals(1L, returnedOrder.getId());
        assertEquals("item-123", returnedOrder.getItemId());
        assertEquals(2, returnedOrder.getQuantity());
        assertEquals(LocalDateTime.parse("2023-01-01T10:00:00"), returnedOrder.getOrderDate());
        assertEquals("PENDING", returnedOrder.getStatus());

        verify(feignOrderClient).getAllOrders();
    }
}