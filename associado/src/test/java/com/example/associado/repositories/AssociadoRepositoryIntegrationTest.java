package com.example.associado.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
	properties = {
		"spring.datasource.url=jdbc:tc:postgresql:latest://associado",
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
	}
)
class AssociadoRepositoryIntegrationTest extends Assertions {

	@Autowired
	private AssociadoRepository repo;

	@Test
	void batataTest() {
		assertTrue(true);
	}
}
