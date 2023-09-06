package com.example.associado.events;

import com.example.common.events.Producer;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class AssociadoEventProducer implements Producer {

  private final AmqpTemplate template;

//  @Autowired
  public AssociadoEventProducer(AmqpTemplate template) {
    this.template = template;
  }

  @Override
  public void send(String id, String message) throws AmqpException {
    this.template.convertAndSend(id, message);
  }

  @Override
  public void send(String id, Object model) {
    this.template.convertAndSend(id, model);
  }

  @Override
  public Object sendAndReceived(String id, String message) throws AmqpException {
    return this.template.convertSendAndReceive(id, message);
  }

  @Override
  public Object sendAndReceived(String id, Object model) throws AmqpException {
    return this.template.convertSendAndReceive(id, model);
  }
}
