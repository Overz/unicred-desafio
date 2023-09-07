package com.example.associado.events;

import com.example.common.events.EventHelpers;
import com.example.common.events.MessageStreaming;
import com.example.common.events.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.common.events.Events.Associado.ASSOCIADO_EXCHANGE;

@Slf4j
@Component
public class AssociadoEventPublisher extends EventHelpers implements Producer<Map<String, Object>> {

	@Autowired
	private RabbitTemplate template;

	@Override
	public void send(String routingKey, Object model) {
		template.convertAndSend(ASSOCIADO_EXCHANGE, routingKey, toMessage(routingKey, model));
	}

	@Override
	public MessageStreaming<Map<String, Object>> exchange(String routingKey, Object model) throws AmqpException {
		log.info("Realizando exchange de '{}' - '{}'", routingKey, model);
		String content = (String) template.convertSendAndReceive(
			ASSOCIADO_EXCHANGE,
			routingKey,
			toMessage(routingKey, model)
		);

		MessageStreaming<Map<String, Object>> messageStreaming = fromMessage(content);
		log.info("Exchange realizado '{}'", messageStreaming);

		return messageStreaming;
	}
}
