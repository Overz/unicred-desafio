package com.example.associado.controllers;

import com.example.common.constants.ProfilesConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@ExtendWith({MockitoExtension.class})
public class HealthControllerTest extends Assertions {

	@InjectMocks
	public HealthController ctrl;

	@Test
	@DisplayName("Deve retornar dados para checagem se o sistema esta funcionando")
	void healthTest() {
		String name = "batata";
		String[] profiles = new String[]{ProfilesConstants.DEV};
		ResponseEntity<Map<String, Object>> res = ctrl.health(name, profiles);

		Map<String, Object> body = res.getBody();
		assertNotNull(body);
		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertTrue(body.containsKey("date"));
		assertEquals(name, body.get("name"));
		assertEquals(profiles, body.get("profiles"));
	}
}
