package org.example.ordersservice.service;

import org.example.ordersservice.model.OrderEntity;
import org.example.ordersservice.repository.OrderRepository;
import org.example.ordersservice.model.OrderDTO;
import org.example.ordersservice.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String ORDER_TOPIC = "order-topic";  // Тема Kafka для заказов

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    // Получить все заказы
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    // Получить заказ по ID
    public Optional<OrderEntity> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Создать новый заказ
    public OrderEntity createOrder(OrderEntity orderEntity) {
        // Здесь можно добавить бизнес-логику для обработки создания заказа
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        // Публикуем событие в Kafka
        sendOrderCreatedEvent(savedOrder);

        return savedOrder;
    }

    // Обновить существующий заказ
    public OrderEntity updateOrder(OrderEntity orderEntity) {
        // Здесь можно добавить проверку на существование заказа перед обновлением
        if (!orderRepository.existsById(orderEntity.getId())) {
            throw new RuntimeException("Order not found");
        }

        return orderRepository.save(orderEntity);
    }

    // Удалить заказ
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    // Публикация события о создании заказа в Kafka
    private void sendOrderCreatedEvent(OrderEntity orderEntity) {
        String orderJson = convertOrderToJson(orderEntity);  // Преобразуем заказ в строку JSON
        kafkaTemplate.send(ORDER_TOPIC, orderJson);  // Отправляем сообщение в Kafka
    }

    // Преобразование объекта заказа в строку JSON (можно использовать библиотеку, например, Jackson)
    private String convertOrderToJson(OrderEntity orderEntity) {
        // Для простоты, здесь можно использовать прямое преобразование
        return "{" +
                "\"id\":" + orderEntity.getId() + "," +
                "\"itemId\":\"" + orderEntity.getItemId() + "\"," +
                "\"quantity\":" + orderEntity.getQuantity() + "," +
                "\"orderDate\":\"" + orderEntity.getOrderDate() + "\"," +
                "\"status\":\"" + orderEntity.getStatus() + "\"" +
                "}";
    }
}
