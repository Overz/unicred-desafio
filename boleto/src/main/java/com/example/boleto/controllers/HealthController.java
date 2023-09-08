package com.example.boleto.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

	@Value("${spring.application.name}")
	private String name;

	@Value("#{environment.getActiveProfiles()}")
	private String[] profiles;

	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public Object health() {
		Map<String, Object> data = new HashMap<>();
		data.put("name", name);
		data.put("date", LocalDateTime.now());
		data.put("profiles", profiles);
		return data;
	}
}
