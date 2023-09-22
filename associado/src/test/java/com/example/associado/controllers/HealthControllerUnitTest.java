package com.example.associado.controllers;

import com.example.associado.SetupTests;
import com.example.associado.mocks.AssociadoMockTest;
import com.example.associado.models.Associado;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@ExtendWith({MockitoExtension.class})
class HealthControllerUnitTest extends Assertions {

	@InjectMocks
	HealthController ctrl;

//	@Mock
//	AssociadoService service;

	Associado associadoMock;

	@BeforeAll
	static void init() {
		SetupTests.setupProperties();
	}

	@BeforeEach
	void setup() {
		associadoMock = AssociadoMockTest.getAssociado();
//		ReflectionTestUtils.setField(ctrl, "service", service);
	}

	@Test
	@DisplayName("deve retornar se o endpoint esta funcionando")
	void healthTest() {
		String nome = "batata";
		String[] profiles = new String[]{nome};

//		Mockito.doReturn(associadoMock).when(service).consultarPorId(Mockito.anyString());

		ResponseEntity<Map<String, Object>> response = ctrl.health(nome, profiles);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		Map<String, Object> body = response.getBody();
		assertNotNull(body);

		assertEquals(nome, body.get("name"));
		assertEquals(profiles, body.get("profiles"));
		assertNotNull(body.get("date"));
//		assertNotNull(body.get("associado"));
	}
}
