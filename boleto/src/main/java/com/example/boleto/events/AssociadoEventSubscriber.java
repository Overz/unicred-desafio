package com.example.boleto.events;

import com.example.boleto.services.BoletoService;
import com.example.common.events.AssociadoEventDTO;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.example.common.events.EventHelpers.toMessage;
import static com.example.common.events.Events.ASSOCIADO_EXCLUIR_QUEUE;

@Slf4j
@Component
public class AssociadoEventSubscriber {

	@Autowired
	private BoletoService service;

	@RabbitListener(queues = {ASSOCIADO_EXCLUIR_QUEUE})
	public String associadoCriadoEvent(String msg) {
		log.info("Mensagem recebida: <{}>", msg);

		try {
			MessageStreaming messageStreaming = MapperUtils.fromJson(msg, MessageStreaming.class);
			AssociadoEventDTO dto = MapperUtils.convert(messageStreaming.getData(), AssociadoEventDTO.class);
			long total = service.contarBoletosNaoPagosPorAssociado(dto.getUuid());
			return toMessage(Collections.singletonMap("ok", total == 0));
		} catch (Exception e) {
			return toMessage(null);
		}
	}
}
