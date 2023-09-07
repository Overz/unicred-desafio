package com.example.associado.events;

import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import com.example.common.events.EventHelpers;
import com.example.common.events.Events;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.common.events.Events.Arquivo.ARQUIVO_QUEUE_NAME;

@Slf4j
@Component
public class ArquivoEventSubscriber {

	@Autowired
	private AssociadoService service;

	@RabbitListener(queues = {ARQUIVO_QUEUE_NAME})
	public String receiveMessage(String msg) {
		log.info("Mensagem recebida <{}>", msg);

		MessageStreaming<Map<String, Object>> messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);

		String subject = messageStreaming.getSubject();
		try {
			String cpfcnpj = (String) messageStreaming.getData().getOrDefault("documento", "");

			return switch (subject) {
				case Events.Arquivo.ARQUIVO_CRIADO_ROUTING_KEY ->
					consultarAssociadoQuandoArquivoProcessado(subject, cpfcnpj);
				default -> EventHelpers.toMessage(subject, null);
			};
		} catch (Exception e) {
			return EventHelpers.toMessage(subject, null);
		}
	}

	private String consultarAssociadoQuandoArquivoProcessado(String subject, String cpfcnpj) {
		Associado associado = service.consultarPorCpfCnpj(cpfcnpj);
		return EventHelpers.toMessage(subject, associado);
	}
}
