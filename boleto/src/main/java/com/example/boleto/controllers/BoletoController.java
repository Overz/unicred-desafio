package com.example.boleto.controllers;

import com.example.boleto.dto.BoletoDTO;
import com.example.boleto.dto.PagarBoletoDTO;
import com.example.boleto.events.BoletoEventPublisher;
import com.example.boleto.models.Boleto;
import com.example.boleto.services.BoletoService;
import com.example.common.errors.BadRequestError;
import com.example.common.events.BoletoEventDTO;
import com.example.common.events.EventHelpers;
import com.example.common.events.Events;
import com.example.common.events.MessageStreaming;
import com.example.common.validations.CpfCnpj;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * - A combinação do identificador do boleto e do identificador do associado deve
 * ser única.
 * - Deve garantir que o associado informado exista no cadastro de associado(API).
 * - O vencimento não pode ser menor que a data atual.
 * - O documento do pagador deve ser válido.
 * - Deve ter tratamento de exceções com mensagens claras do motivo da falha.
 * - Deve expor um endpoint de busca de boletos por uuid de associado.
 * - Deve expor um endpoint de pagamento de boleto através do documento do
 * associado, identificador do boleto e valor.
 * - Não deve permitir pagamentos de boleto com valor divergente do cadastrado.
 * - Não deve permitir pagamentos de boleto já pagos.
 */
@Slf4j
@RestController
@RequestMapping("/boleto")
public class BoletoController {

	@Autowired
	private BoletoService service;

	@Autowired
	private BoletoEventPublisher publisher;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<BoletoDTO> criarBoleto(@RequestBody @Valid BoletoDTO dto) {
		if (CpfCnpj.isInvalid(dto.getDocumentoPagador())) {
			throw new BadRequestError("CPF/CNPJ inválido");
		}

		if (dto.getVencimento().isBefore(LocalDateTime.now())) {
			throw new BadRequestError("Vencimento do boleto menor que a data atual");
		}

		BoletoEventDTO event = new BoletoEventDTO(dto.getId(), dto.getValor(), dto.getVencimento(), dto.getAssociado(), dto.getDocumentoPagador(), dto.getNomePagador(), dto.getNomeFantasiaPagador(), dto.getSituacao());
		String message = EventHelpers.toMessage(Events.BOLETO_CONSULTAR_ASSOCIADO_POR_ID_SUBJECT, event);
		MessageStreaming messageStreaming = publisher.exchange(Events.BOLETO_CONSULTAR_ASSOCIADO_ROUTING_KEY, message);

		if (messageStreaming.getData() == null) {
			throw new BadRequestError("Associado não encontrado");
		}

		Boleto boleto = service.criarBoleto(dto.toEntity());
		return ResponseEntity.status(HttpStatus.CREATED).body(boleto.toDTO());
	}

	@PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarBoleto(@PathVariable("id") String boletoId, @RequestBody @Valid BoletoDTO dto) {
		service.atualizarBoleto(boletoId, dto.toEntity());
	}

	@DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<String, Object>> excluirBoleto(@PathVariable("id") String boletoId) {
		service.excluirBoletoPorId(boletoId);
		return ResponseEntity.ok(Collections.singletonMap("ok", true));
	}

	@GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<BoletoDTO> consultarBoleto(@PathVariable("id") String boletoId) {
		Boleto boleto = service.consultarBoletoPorId(boletoId);
		return ResponseEntity.ok(boleto.toDTO());
	}

	@GetMapping(path = "/{id}/associado", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<BoletoDTO>> listarBoletosPorAssociado(@PathVariable("id") String id) {
		List<Boleto> list = service.listarBoletosPorAssociado(id);
		return ResponseEntity.ok(list.stream().map(Boleto::toDTO).toList());
	}

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<BoletoDTO>> listar() {
		List<Boleto> list = service.listar();
		return ResponseEntity.ok(list.stream().map(Boleto::toDTO).toList());
	}

	@PutMapping(path = "/{id}/pagar", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void pagarBoleto(
		@PathVariable("id") String id,
		@RequestBody @Valid PagarBoletoDTO dto
	) {
		if (CpfCnpj.isInvalid(dto.getDocumento())) {
			throw new BadRequestError("CPF/CNPJ inválido");
		}

		service.pagarBoleto(id, dto.getDocumento(), dto.getValor());
	}
}
