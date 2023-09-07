package com.example.boleto.events;

import com.example.boleto.services.BoletoService;
import com.example.common.events.EventHelpers;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.example.common.events.Events.Associado.*;

@Slf4j
@Component
public class AssociadoEventSubscriber {

	@Autowired
	private BoletoService service;

	@RabbitListener(queues = {ASSOCIADO_QUEUE_NAME})
	public String associadoCriadoEvent(String msg) {
		log.info("Mensagem recebida: <{}>", msg);

		MessageStreaming<Map<String, Object>> messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);
		String idAssociado = (String) messageStreaming.getData().getOrDefault("id", "");

		String subject = messageStreaming.getSubject();
		String def = EventHelpers.toMessage(subject, null);
		try {
			return switch (subject) {
				case ASSOCIADO_CRIADO_ROUTING_KEY -> def;
				case ASSOCIADO_ATUALIZADO_ROUTING_KEY -> def;
				case ASSOCIADO_EXCLUIR_ROUTING_KEY ->
					verificarPossibilidadeDeExclusaoParaAssociado(subject, idAssociado);
				default -> EventHelpers.toMessage(subject, null);
			};
		} catch (Exception e) {
			return EventHelpers.toMessage(subject, null);
		}
	}

	private String verificarPossibilidadeDeExclusaoParaAssociado(String subject, String cpfcnpj) {
		long total = service.contarBoletosNaoPagosPorAssociado(cpfcnpj);
		return EventHelpers.toMessage(subject, Collections.singletonMap("ok", total > 0));
	}
}
