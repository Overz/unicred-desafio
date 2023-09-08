package com.example.boleto.events;

import com.example.boleto.models.Boleto;
import com.example.boleto.services.BoletoService;
import com.example.common.constants.BoletoConstants;
import com.example.common.events.*;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.example.common.events.Events.ARQUIVO_PROCESSADO_QUEUE;

@Slf4j
@Component
public class ArquivoEventSubscriber {

	@Autowired
	private BoletoEventPublisher publisher;

	@Autowired
	private BoletoService service;

	@Value("${custom.boleto.vencimento}")
	private Long vencimentoPadrao;

	@Transactional
	@RabbitListener(queues = {ARQUIVO_PROCESSADO_QUEUE})
	public String arquivoProcessadoEvent(String msg) {
		log.info("Message received <{}>", msg);

		try {
			MessageStreaming messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);
			ArquivoEventDTO arquivoEventDTO = MapperUtils.convert(messageStreaming.getData(), ArquivoEventDTO.class);

			BoletoEventDTO boletoEventDTO = new BoletoEventDTO();
			boletoEventDTO.setDocumento_pagador(arquivoEventDTO.getDocumento().substring(3, 14).trim());

			String message = EventHelpers.toMessage(Events.BOLETO_CONSULTAR_ASSOCIADO_POR_CPFCNPJ_SUBJECT, boletoEventDTO);
			messageStreaming = publisher.exchange(Events.BOLETO_CONSULTAR_ASSOCIADO_ROUTING_KEY, message);
			if (messageStreaming.getData() == null) {
				return "";
			}

			AssociadoEventDTO associadoEventDTO = MapperUtils.convert(messageStreaming.getData(), AssociadoEventDTO.class);

			Boleto boleto = Boleto
				.builder()
				.uuid(arquivoEventDTO.getId() + associadoEventDTO.getUuid())
				.documento_pagador(arquivoEventDTO.getDocumento())
				.valor(new BigDecimal(arquivoEventDTO.getValor()))
				.uuid_associado(associadoEventDTO.getUuid())
				.nome_pagador(associadoEventDTO.getNome())
				.vencimento(LocalDateTime.now().plusDays(vencimentoPadrao))
				.situacao(BoletoConstants.PENDENTE)
				.build();

			service.criarBoleto(boleto);

			return EventHelpers.toMessage(Collections.singletonMap("ok", true));
		} catch (Exception e) {
			return EventHelpers.toMessage(null);
		}
	}
}
