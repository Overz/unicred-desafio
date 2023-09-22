package com.example.associado.controllers;

import com.example.associado.dto.AssociadoDTO;
import com.example.associado.events.AssociadoEventPublisher;
import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import com.example.common.errors.BadRequestError;
import com.example.common.events.AssociadoEventDTO;
import com.example.common.events.Events;
import com.example.common.events.MessageStreaming;
import com.example.common.mappers.MapperUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/associado")
@AllArgsConstructor
public class AssociadoController {
	private final AssociadoService service;
	private final AssociadoEventPublisher publisher;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AssociadoDTO> criarAssociado(@RequestBody @Valid AssociadoDTO dto) {
		Associado associado = service.criarAssociado(dto.toEntity());
		return ResponseEntity.status(HttpStatus.CREATED).body(associado.toDTO());
	}

	@PutMapping(path = "/{cpfcnpj}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarAssociado(
		@PathVariable("cpfcnpj") String cpfcnpj,
		@RequestBody @Valid AssociadoDTO dto
	) {
		Associado associado = service.atualizarAssociado(cpfcnpj, dto.toEntity());
		log.info("Associado '{}' atualizado '{}'", dto, associado);
	}

	@DeleteMapping(path = "/{cpfcnpj}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<String, Object>> excluir(@PathVariable("cpfcnpj") String cpfcnpj) {
		Associado associado = service.consultarPorCpfCnpj(cpfcnpj);
		AssociadoEventDTO event = new AssociadoEventDTO(associado.getUuid(), associado.getDocumento(), associado.getTipo_pessoa(), associado.getNome());
		MessageStreaming messageStreaming = publisher.exchange(Events.ASSOCIADO_EXCLUIR_ROUTING_KEY, event);

		Map<String, Boolean> dto = MapperUtils.convert(messageStreaming.getData(), Map.class);
		Boolean ok = dto.get("ok");
		if (!ok) {
			throw new BadRequestError("Exclusão não pode ser realizada, boletos pendentes!");
		}

		service.excluir(cpfcnpj);
		log.info("Associado excluido '{}'", cpfcnpj);
		return ResponseEntity.ok(Collections.singletonMap("ok", true));
	}

	@GetMapping(path = "/{cpfcnpj}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AssociadoDTO> consultarPorCpfCnpj(@PathVariable("cpfcnpj") String cpfcnpj) {
		Associado a = service.consultarPorCpfCnpj(cpfcnpj);
		return ResponseEntity.ok(a.toDTO());
	}

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<AssociadoDTO> listar() {
		List<Associado> list = service.listar();
		return list.stream().map(Associado::toDTO).toList();
	}
}
