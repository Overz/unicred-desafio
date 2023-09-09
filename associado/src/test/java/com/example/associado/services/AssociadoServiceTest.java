package com.example.associado.services;

import com.example.associado.models.Associado;
import com.example.associado.repositories.AssociadoRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.example.associado.mocks.AssociadoMockTest.getAssociado;

@ExtendWith({MockitoExtension.class})
public class AssociadoServiceTest extends Assertions {

	@InjectMocks
	AssociadoService service;

	@Mock
	AssociadoRepository repo;

	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(service, "repo", repo);
	}

	@Test
	@DisplayName("deve cadastrar um novo associado")
	void criarAssociadoTest() {
		Associado mock = getAssociado();

		Mockito.doReturn(mock).when(repo).save(Mockito.any(Associado.class));

		Associado associado = service.criarAssociado(mock);
		assertTrue(Matchers.equalToObject(mock).matches(associado));
	}
}
