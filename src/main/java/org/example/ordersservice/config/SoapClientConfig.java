package org.example.ordersservice.config;

import org.example.ordersservice.client.NotificationSoapClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.example.notificationservice");
        return marshaller;
    }

    @Bean
    public NotificationSoapClient notificationSoapClient(Jaxb2Marshaller marshaller) {
        NotificationSoapClient client = new NotificationSoapClient();
        client.setDefaultUri("http://localhost:8081/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}