package com.example.associado.events;

import com.example.common.events.Events;
import com.example.common.events.Producer;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssociadoEventProducer implements Producer {

	@Autowired
	private RabbitTemplate template;

	@Override
	public void send(String routingKey, String message) throws AmqpException {
		this.template.convertAndSend(Events.Associado.ASSOCIADO_TOPIC, routingKey, message);
	}

	@Override
	public void send(String routingKey, Object model) {
		this.template.convertAndSend(Events.Associado.ASSOCIADO_TOPIC, routingKey, model);
	}

	@Override
	public Object sendAndReceived(String routingKey, String message) throws AmqpException {
		return this.template.convertSendAndReceive(Events.Associado.ASSOCIADO_TOPIC, routingKey, message);
	}

	@Override
	public Object sendAndReceived(String routingKey, Object model) throws AmqpException {
		return this.template.convertSendAndReceive(Events.Associado.ASSOCIADO_TOPIC, routingKey, model);
	}
}
