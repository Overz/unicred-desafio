package com.example.boleto.events;

import com.example.common.events.EventHelpers;
import com.example.common.events.Events;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.common.events.Events.Arquivo.ARQUIVO_PROCESSADO_ROUTING_KEY;
import static com.example.common.events.Events.Arquivo.ARQUIVO_QUEUE_NAME;

@Slf4j
@Component
public class ArquivoEventSubscriber {

	@RabbitListener(queues = {ARQUIVO_QUEUE_NAME})
	public String receiveMessage(String msg) {
		log.info("Message received <{}>", msg);

		MessageStreaming<Map<String, Object>> messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);

		String subject = messageStreaming.getSubject();
		try {
			return switch (subject) {
				case ARQUIVO_PROCESSADO_ROUTING_KEY -> "";
				default -> EventHelpers.toMessage(subject, null);
			};
		} catch (Exception e) {
			return EventHelpers.toMessage(subject, null);
		}
	}

	private String salvarArquivoProcessado() {

		return "";
	}
}
