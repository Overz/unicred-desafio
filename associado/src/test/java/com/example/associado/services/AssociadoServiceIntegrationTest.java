package com.example.associado.services;

import com.example.associado.DatabaseHelperTest;
import com.example.associado.constants.DatasetConstantsTest;
import com.example.associado.mocks.AssociadoMockTest;
import com.example.associado.models.Associado;
import com.example.associado.repositories.AssociadoRepository;
import com.example.common.constants.ProfilesConstants;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles({ProfilesConstants.INTEGRATION_TEST})
@TestPropertySource({"classpath:integration-test.properties"})
public class AssociadoServiceIntegrationTest extends Assertions {

	@Autowired
	AssociadoService service;

	@Autowired
	AssociadoRepository repo;

	@BeforeEach
	void setup() {
		DatabaseHelperTest
			.getInstance()
			.execute(DatasetConstantsTest.ASSOCIADO_DATASET, DatabaseOperation.CLEAN_INSERT);
	}

	@Test
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
}
