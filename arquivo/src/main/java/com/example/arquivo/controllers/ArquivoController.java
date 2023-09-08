package com.example.arquivo.controllers;

import com.example.arquivo.dto.ArquivoComPathDTO;
import com.example.arquivo.services.ArquivoService;
import com.example.common.errors.BadRequestError;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {

	@Autowired
	private ArquivoService service;

	@Value("${custom.processamento.tipo-arquivo}")
	private String fileFilter;

	@PostMapping(path = "/processar-caminho", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<String, Boolean>> processar(@RequestBody @Valid ArquivoComPathDTO dto) {
		File[] arquivos = new File(dto.getCaminho()).listFiles((dir, name) -> name.endsWith(fileFilter));
		if (arquivos == null || arquivos.length == 0) {
			throw new BadRequestError("Diretório de arquivos não encontrado e/ou vazio para arquivos do tipo '" + fileFilter + "'");
		}

		service.processarArquivoParalel(arquivos);

		return ResponseEntity.ok(Collections.singletonMap("ok", true));
	}

//	@PostMapping(
//		path = "/processar-varios",
//		consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
//	)
//	public ResponseEntity<Map<String, Object>> processarMultiPart(@RequestParam("arquivos") MultipartFile[] files) {
//		if (files == null || files.length == 0) {
//			throw new BadRequestError("Propriedade 'arquivos' não fornecida, deve ser fornecido ao menos um arquivo");
//		}
//
//		return ResponseEntity.ok(Collections.singletonMap("ok", true));
//	}
}
