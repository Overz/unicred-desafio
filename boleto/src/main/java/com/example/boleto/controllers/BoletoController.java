package com.example.boleto.controllers;

import com.example.boleto.dto.BoletoDTO;
import com.example.boleto.dto.PagarBoletoDTO;
import com.example.boleto.events.BoletoEventPublisher;
import com.example.boleto.models.Boleto;
import com.example.boleto.services.BoletoService;
import com.example.common.constants.ProfilesConstants;
import com.example.common.errors.BadRequestError;
import com.example.common.events.MessageStreaming;
import com.example.common.validations.CpfCnpj;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.example.common.events.Events.Boleto.*;

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

	/**
	 * Endpoint para teste se a mensagem esta funcionando
	 * Funciona apenas em Desenvolvimento
	 *
	 * @param routingKey id/subject
	 * @param model      any
	 */
	@Profile(ProfilesConstants.DEV)
	@PostMapping("/message/{routingKey}")
	public ResponseEntity<Object> teste(
		@PathVariable("routingKey") String routingKey,
		@RequestBody Map<String, Object> model
	) {
		log.info("Enviando mensagem, routing: '{}'", routingKey);
		MessageStreaming<Map<String, Object>> data = publisher.exchange(routingKey, model);

		log.info("Resposta recebida, routing: '{}', json: '{}'", routingKey, data);
		return ResponseEntity.ok().body(data);
	}

	@PostMapping
	public ResponseEntity<BoletoDTO> criarBoleto(@RequestBody @Valid BoletoDTO dto) {
		if (CpfCnpj.isInvalid(dto.getDocumentoPagador())) {
			throw new BadRequestError("CPF/CNPJ inválido");
		}

		MessageStreaming<Map<String, Object>> messageStreaming = publisher.exchange(BOLETO_CRIADO_ROUTING_KEY, dto);

		if (messageStreaming.getData() == null) {
			throw new BadRequestError("Associado não encontrado");
		}

		Boleto boleto = service.criarBoleto(dto.toEntity());
		return ResponseEntity.status(HttpStatus.CREATED).body(boleto.toDTO());
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarBoleto(@PathVariable("id") String boletoId, @RequestBody @Valid BoletoDTO dto) {
		service.atualizarBoleto(boletoId, dto.toEntity());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void excluirBoleto(@PathVariable("id") String boletoId) {
		service.excluirBoletoPorId(boletoId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BoletoDTO> consultarBoleto(@PathVariable("id") String boletoId) {
		Boleto boleto = service.consultarBoletoPorId(boletoId);
		return ResponseEntity.ok(boleto.toDTO());
	}

	@GetMapping("/{id}/associado")
	public ResponseEntity<List<BoletoDTO>> listarBoletosPorAssociado(@PathVariable("id") String id) {
		List<Boleto> list = service.listarBoletosPorAssociado(id);
		return ResponseEntity.ok(list.stream().map(Boleto::toDTO).toList());
	}

	@GetMapping
	public ResponseEntity<List<BoletoDTO>> listar() {
		List<Boleto> list = service.listar();
		return ResponseEntity.ok(list.stream().map(Boleto::toDTO).toList());
	}

	@PutMapping("/{id}/pagar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void pagarPorIdBoletoCpfCnpjAssociadoMaisValor(
		@PathVariable("id") String id,
		@RequestBody @Valid PagarBoletoDTO dto
	) {
		if (CpfCnpj.isInvalid(dto.getDocumento())) {
			throw new BadRequestError("CPF/CNPJ inválido");
		}

		service.pagarBoleto(id, dto.getDocumento(), dto.getValor());
	}
}
