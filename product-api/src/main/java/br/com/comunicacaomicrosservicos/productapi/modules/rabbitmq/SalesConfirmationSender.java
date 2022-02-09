package br.com.comunicacaomicrosservicos.productapi.modules.rabbitmq;

import br.com.comunicacaomicrosservicos.productapi.modules.sales.dto.SalesConfirmationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SalesConfirmationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    public void sendSalesConfirmationMessage(SalesConfirmationDto salesConfirmationDto) {
        try {
            log.info("Sending message: {}", new ObjectMapper().writeValueAsString(salesConfirmationDto));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, salesConfirmationDto);
            log.info("Message was sent succesfully");
        } catch (Exception ex) {
            log.info("Error while trying to send sales confirmation message: ", ex);
        }
    }
}
