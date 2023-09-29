package com.example.associado.events;

import com.example.associado.mocks.AssociadoMockTest;
import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import com.example.common.events.AssociadoEventDTO;
import com.example.common.events.BoletoEventDTO;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.example.common.events.EventHelpers.fromMessage;
import static com.example.common.events.EventHelpers.toMessage;
import static com.example.common.events.Events.BOLETO_CONSULTAR_ASSOCIADO_POR_CPFCNPJ_SUBJECT;
import static com.example.common.events.Events.BOLETO_CONSULTAR_ASSOCIADO_POR_ID_SUBJECT;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class BoletoEventSubscriberTest extends Assertions {

	@InjectMocks
	BoletoEventSubscriber subscriber;
	@Mock
	AssociadoService service;
	BoletoEventDTO event;
	Associado mock = AssociadoMockTest.getAssociado();

	@BeforeEach
	void setup() {
		event = new BoletoEventDTO();
		event.setUuid_associado("123");
		event.setDocumento_pagador("123");
		Mockito.when(service.consultarPorId(Mockito.anyString())).thenReturn(mock);
		Mockito.when(service.consultarPorCpfCnpj(Mockito.anyString())).thenReturn(mock);
	}

	AssociadoEventDTO send(String subject) {
		String message = subscriber.handler(toMessage(subject, event));
		MessageStreaming<?> streaming = fromMessage(message);
		return MapperUtils.convert(streaming.getData(), AssociadoEventDTO.class);
	}

	@Test
	@DisplayName("deve retornar dados do associado consultado por ID")
	void sucesso_consultar_associado_por_id() {
		AssociadoEventDTO dto = send(BOLETO_CONSULTAR_ASSOCIADO_POR_ID_SUBJECT);
		assertEquals(mock.getUuid(), dto.getUuid());
		assertEquals(mock.getDocumento(), dto.getDocumento());
		assertEquals(mock.getNome(), dto.getNome());
		assertEquals(mock.getTipo_pessoa(), dto.getTipo_pessoa());
	}

	@Test
	@DisplayName("deve retornar dados do asociado consultado por CPF/CNPJ")
	void sucesso_consultar_associado_por_cpfcnpj() {
		AssociadoEventDTO dto = send(BOLETO_CONSULTAR_ASSOCIADO_POR_CPFCNPJ_SUBJECT);
		assertEquals(mock.getUuid(), dto.getUuid());
		assertEquals(mock.getDocumento(), dto.getDocumento());
		assertEquals(mock.getNome(), dto.getNome());
		assertEquals(mock.getTipo_pessoa(), dto.getTipo_pessoa());
	}

	@Test
	@DisplayName("deve retornar o conteúdo vazio caso o subject não for encontrado")
	void error_subject_nao_implementado() {
		AssociadoEventDTO dto = send("");
		assertNull(dto);
	}

	@Test
	@DisplayName("deve retornar conteúdo vazio caso ocorra um erro no processamento da mensagem")
	void erro_processando_mensagem() {
		event = null;
		AssociadoEventDTO dto = send("");
		assertNull(dto);
	}
}
