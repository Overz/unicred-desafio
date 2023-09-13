package com.example.associado.services;

import com.example.associado.mocks.AssociadoMockTest;
import com.example.associado.models.Associado;
import com.example.associado.repositories.AssociadoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@SpringBootTest(
	properties = {
		"spring.datasource.url=jdbc:tc:postgresql:latest://associado",
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
	}
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AssociadoServiceIntegrationTest extends Assertions {

	@Autowired
	AssociadoService service;

	@Autowired
	AssociadoRepository repo;

	@BeforeEach
	void setup() {
	}

	@AfterEach
	void clean() {
		repo.deleteAll();
	}

	@Test
	@Sql("classpath:sql/setup.sql")
	@DisplayName("deve cadastar um novo associado")
	void criarAssociadoIntegrationTest() {
		Associado mock = AssociadoMockTest.getAssociado();
		mock.setUuid(null);

		Associado associado = service.criarAssociado(mock);
		assertNotNull(associado.getUuid());
		assertEquals(mock.getDocumento(), associado.getDocumento());
		assertEquals(mock.getNome(), associado.getNome());
		assertEquals(mock.getTipo_pessoa(), associado.getTipo_pessoa());

		Optional<Associado> opt = repo.findById(associado.getUuid());
		assertTrue(opt.isPresent());

		Associado persisted = opt.get();
		assertEquals(associado.getUuid(), persisted.getUuid());
		assertEquals(associado.getDocumento(), persisted.getDocumento());
		assertEquals(associado.getTipo_pessoa(), persisted.getTipo_pessoa());
		assertEquals(associado.getNome(), persisted.getNome());
	}

	@Test
	@Sql("classpath:sql/setup.sql")
	@DisplayName("deve atualizar um associado")
	void atualizrAssociadoIntegrationTest() {
		Optional<Associado> opt = repo.consultarPorCpfCnpj(AssociadoMockTest.DOC_1);

		assertTrue(opt.isPresent());

		Associado associado = opt.get();
		assertEquals(AssociadoMockTest.DOC_1, associado.getDocumento());

		final String cpf = "01234567890";
		associado.setDocumento(cpf);

		associado = service.atualizarAssociado(AssociadoMockTest.DOC_1, associado);
		assertEquals(cpf, associado.getDocumento());
	}
}
