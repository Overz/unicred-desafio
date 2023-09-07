package com.example.associado.controllers;

import com.example.associado.dto.AssociadoDTO;
import com.example.associado.events.AssociadoEventPublisher;
import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import com.example.common.constants.ProfilesConstants;
import com.example.common.errors.BadRequestError;
import com.example.common.events.Events;
import com.example.common.events.MessageStreaming;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.common.events.Events.Associado.*;

@Slf4j
@RestController
@RequestMapping("/associado")
public class AssociadoController {

	@Autowired
	private AssociadoService service;

	@Autowired
	private AssociadoEventPublisher publisher;

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
	public ResponseEntity<AssociadoDTO> criarAssociado(@RequestBody @Valid AssociadoDTO dto) {
		Associado associado = service.criarAssociado(dto.toEntity());
		publisher.exchange(ASSOCIADO_CRIADO_ROUTING_KEY, associado);
		return ResponseEntity.status(HttpStatus.CREATED).body(associado.toDTO());
	}

	@PutMapping("/{cpfcnpj}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAssociado(
		@PathVariable("cpfcnpj") String cpfcnpj,
		@RequestBody @Valid AssociadoDTO dto
	) {
		Associado associado = service.atualizarAssociado(cpfcnpj, dto.toEntity());
		publisher.send(ASSOCIADO_ATUALIZADO_ROUTING_KEY, associado);
		log.info("Associado '{}' atualizado '{}'", dto, associado);
	}

	@DeleteMapping("/{cpfcnpj}")
	@ResponseStatus(HttpStatus.OK)
	public void excluir(@PathVariable("cpfcnpj") String cpfcnpj) {
		log.debug("Solicitando remoção do documento '{}'", cpfcnpj);

		Associado associado = service.consultarPorCpfCnpj(cpfcnpj);
		MessageStreaming<Map<String, Object>> messageStreaming = publisher.exchange(ASSOCIADO_EXCLUIR_ROUTING_KEY, associado);

		Boolean ok = (Boolean) messageStreaming.getData().getOrDefault("ok", false);
		if (!ok) {
			throw new BadRequestError("Exclusão não pode ser realizada, boleto não pago!");
		}

		service.excluir(cpfcnpj);
		log.info("Associado excluido '{}'", cpfcnpj);
	}

	@GetMapping("/{cpfcnpj}")
	public ResponseEntity<AssociadoDTO> consultarPorCpfCnpj(@PathVariable("cpfcnpj") String cpfcnpj) {
		Associado a = service.consultarPorCpfCnpj(cpfcnpj);
		return ResponseEntity.ok(a.toDTO());
	}

	@GetMapping
	public List<AssociadoDTO> listar() {
		List<Associado> list = service.listar();
		return list.stream().map(Associado::toDTO).toList();
	}
}
