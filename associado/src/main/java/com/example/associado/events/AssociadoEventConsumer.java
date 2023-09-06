package com.example.associado.events;

import com.example.common.events.Events;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssociadoEventConsumer {

//  @RabbitListener(queues = {""})
  public void receiveMessage(String message) {
    log.info("Message received <{}>", message);
  }
}
