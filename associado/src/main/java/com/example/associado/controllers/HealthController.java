package com.example.associado.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

//	@Autowired
//	private AssociadoService service;

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<String, Object>> health(
		@Value("${spring.application.name}") String name,
		@Value("#{environment.getActiveProfiles()}") String[] profiles
	) {
		Map<String, Object> data = new HashMap<>();
		data.put("name", name);
		data.put("date", LocalDateTime.now());
		data.put("profiles", profiles);

		// Associado associado = service.consultarPorId("123");
		// data.put("associado", associado);

		return ResponseEntity.ok(data);
	}
}
