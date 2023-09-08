package com.example.associado.events;

import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import com.example.common.events.*;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.common.events.Events.BOLETO_CONSULTAR_ASSOCIADO_QUEUE;

@Slf4j
@Component
public class BoletoEventSubscriber {

	@Autowired
	private AssociadoService service;

	@RabbitListener(queues = {BOLETO_CONSULTAR_ASSOCIADO_QUEUE})
	public String receiveMessage(String msg) {
		log.info("Message received <{}>", msg);

		try {
			MessageStreaming messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);
			String subject = messageStreaming.getSubject();
			return switch (subject) {
				case Events.BOLETO_CONSULTAR_ASSOCIADO_POR_ID_SUBJECT -> {
					BoletoEventDTO dto = MapperUtils.convert(messageStreaming.getData(), BoletoEventDTO.class);
					Associado associado = service.consultarPorId(dto.getUuid_associado());
					yield EventHelpers.toMessage(associado);
				}
				case Events.BOLETO_CONSULTAR_ASSOCIADO_POR_CPFCNPJ_SUBJECT -> {
					BoletoEventDTO dto = MapperUtils.convert(messageStreaming.getData(), BoletoEventDTO.class);
					Associado associado = service.consultarPorCpfCnpj(dto.getDocumento_pagador());
					AssociadoEventDTO event = new AssociadoEventDTO(
						associado.getUuid(),
						associado.getDocumento(),
						associado.getTipo_pessoa(),
						associado.getNome()
					);
					yield EventHelpers.toMessage(subject, event);
				}
				default -> "";
			};
		} catch (Exception e) {
			return EventHelpers.toMessage(null);
		}
	}
}
