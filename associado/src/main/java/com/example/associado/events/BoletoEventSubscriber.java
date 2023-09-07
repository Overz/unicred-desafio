package com.example.associado.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssociadoEventSubscriber {

//  @RabbitListener(queues = {""})
  public void receiveMessage(String message) {
    log.info("Message received <{}>", message);
  }
}
