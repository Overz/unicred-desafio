package com.example.associado.events;

import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import com.example.common.events.EventHelpers;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.common.events.Events.Boleto.*;

@Slf4j
@Component
public class BoletoEventSubscriber {

	@Autowired
	private AssociadoService service;

	@RabbitListener(queues = {BOLETO_QUEUE_NAME})
	public String receiveMessage(String msg) {
		log.info("Message received <{}>", msg);

		MessageStreaming<Map<String, Object>> messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);

		String subject = messageStreaming.getSubject();
		try {
			String cpfcnpj = (String) messageStreaming.getData().getOrDefault("documentoPagador", "");
			return switch (subject) {
				case BOLETO_CRIADO_ROUTING_KEY -> consultarAssociadoQuandoBoletoCriado(subject, cpfcnpj);
				case BOLETO_ATUALIZADO_ROUTING_KEY ->
					atualizrDadosAssociadoQuandoBoletoAtualizado(subject, cpfcnpj);
				case BOLETO_EXCLUIR_ROUTING_KEY -> excluirAssociadoQuandoBoletoExcluido(subject, cpfcnpj);
				default -> EventHelpers.toMessage(subject, null);
			};
		} catch (Exception e) {
			return EventHelpers.toMessage(subject, null);
		}
	}

	private String consultarAssociadoQuandoBoletoCriado(String subject, String cpfcnpj) {
		Associado associado = service.consultarPorCpfCnpj(cpfcnpj);
		return EventHelpers.toMessage(subject, associado);
	}

	// TODO: Implementar
	private String atualizrDadosAssociadoQuandoBoletoAtualizado(String subject, String cnfcnpj) {
		return EventHelpers.toMessage(subject, null);
	}

	// TODO: Implementar
	private String excluirAssociadoQuandoBoletoExcluido(String subject, String cpfcnpj) {
		return EventHelpers.toMessage(subject, null);
	}
}
