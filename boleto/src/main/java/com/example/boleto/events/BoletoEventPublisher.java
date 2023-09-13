package com.example.boleto.events;

import com.example.common.events.EventHelpers;
import com.example.common.events.MessageStreaming;
import com.example.common.events.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.common.events.Events.BOLETO_EXCHANGE;

@Slf4j
@Component
public class BoletoEventPublisher extends EventHelpers implements Publisher {

  private final RabbitTemplate template;

  @Autowired
  public BoletoEventPublisher(RabbitTemplate template) {
    template.setExchange(BOLETO_EXCHANGE);
    this.template = template;
  }

  @Override
  public void send(Object model) throws RuntimeException {
    template.convertAndSend(model);
  }

  @Override
  public void send(String routingKey, Object model) throws RuntimeException {
    template.convertAndSend(routingKey, toMessage(routingKey, model));
  }

  @Override
  public void send(String routingKey, String message) throws RuntimeException {
    template.convertAndSend(routingKey, message);
  }

  @Override
  public void send(String exchange, String routingKey, String message) throws RuntimeException {
    template.convertAndSend(exchange, routingKey, message);
  }

  @Override
  public void send(String exchange, String routingKey, Object model) throws RuntimeException {
    template.convertAndSend(exchange, routingKey, toMessage(routingKey, model));
  }

  @Override
  public MessageStreaming exchange(Object model) throws RuntimeException {
    return fromMessage((String) template.convertSendAndReceive(toMessage(model)));
  }

  @Override
  public MessageStreaming exchange(String routingKey, Object model) throws AmqpException {
    return fromMessage((String) template.convertSendAndReceive(routingKey, toMessage(routingKey, model)));
  }

  @Override
  public MessageStreaming exchange(String routingKey, String message) throws RuntimeException {
    return fromMessage((String) template.convertSendAndReceive(routingKey, message));
  }

  @Override
  public MessageStreaming exchange(String exchange, String routingKey, String message) throws RuntimeException {
    return fromMessage((String) template.convertSendAndReceive(exchange, routingKey, message));
  }

  @Override
  public MessageStreaming exchange(String exchange, String routingKey, Object model) throws RuntimeException {
    return fromMessage((String) template.convertSendAndReceive(exchange, routingKey, toMessage(routingKey, model)));
  }
}
