package com.example.associado.controllers;

import com.example.associado.events.AssociadoEventPublisher;
import com.example.associado.services.AssociadoService;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AssociadoControllerIntegrationTest extends Assertions {

	@MockBean
	AssociadoService service;

	@MockBean
	AssociadoEventPublisher publisher;
}
