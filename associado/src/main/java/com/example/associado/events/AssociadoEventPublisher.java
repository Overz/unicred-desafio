package com.example.associado.events;

import com.example.common.events.EventHelpers;
import com.example.common.events.MessageStreaming;
import com.example.common.events.Publisher;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.common.events.Events.ASSOCIADO_EXCHANGE;

@Slf4j
@Component
@AllArgsConstructor
public class AssociadoEventPublisher extends EventHelpers implements Publisher {
	private final RabbitTemplate template;

	@PostConstruct
	private void post() {
		this.template.setExchange(ASSOCIADO_EXCHANGE);
	}

	@Override
	public void send(Object model) throws RuntimeException {
		template.convertAndSend(toMessage(model));
	}

	@Override
	public void send(String routingKey, Object model) {
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
