package org.example.ordersservice.client;

import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.CreateOrderRequest;
import org.example.notificationservice.CreateOrderResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Slf4j
public class NotificationSoapClient extends WebServiceGatewaySupport {

    public CreateOrderResponse sendOrder(CreateOrderRequest request) {
        log.info("Sending order: {}", request);
        String endpoint = "http://localhost:8081/ws";
        CreateOrderResponse response = (CreateOrderResponse) getWebServiceTemplate().marshalSendAndReceive(endpoint, request);
        log.info("Received response: {}", response);
        return response;
    }
}