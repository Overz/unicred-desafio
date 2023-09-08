package com.example.boleto.controllers;

import com.example.boleto.events.BoletoEventPublisher;
import com.example.common.constants.ProfilesConstants;
import com.example.common.events.EventHelpers;
import com.example.common.events.MessageStreaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Profile(ProfilesConstants.DEV)
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private BoletoEventPublisher publisher;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> teste(@RequestBody Map<String, Object> body) {
		String exchange = (String) body.get("exchange");
		String routingKey = (String) body.get("routing");
		String subject = (String) body.get("subject");
		Object data = body.getOrDefault("data", Collections.singletonMap("ok", true));

		String message = EventHelpers.toMessage(subject, data);
		MessageStreaming messageStreaming = publisher.exchange(exchange, routingKey, message);
		log.info("Resposta recebida: '{}' - '{}' - '{}'", exchange, routingKey, messageStreaming);

		return ResponseEntity.ok(Collections.singletonMap("ok", true));
	}
}
