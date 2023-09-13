package com.example.associado.services;

import com.example.associado.matchers.ErrorMatcher;
import com.example.associado.mocks.AssociadoMockTest;
import com.example.associado.models.Associado;
import com.example.associado.repositories.AssociadoRepository;
import com.example.common.errors.BadRequestError;
import com.example.common.errors.NotFoundError;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@SpringBootTest(
    properties = {
        "spring.datasource.url=jdbc:tc:postgresql:latest://associado"
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

  @Nested
  @Sql("classpath:sql/setup.sql")
  class CriarAssociado {
    @Test
    @DisplayName("deve cadastar um novo associado")
    void sucesso_test() {
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
    @DisplayName("deve retornar BadRequest se o CPF/CNPJ for inválido")
    void erro_cpfcnpj_invalido_test() {
      String documento = "123";
      Associado mock = AssociadoMockTest.getAssociado();
      mock.setDocumento(documento);

      ErrorMatcher.toThrow(
          new BadRequestError("CPF/CNPJ não aceito"),
          () -> service.criarAssociado(mock)
      );
    }

    @Test
    @DisplayName("deve retornar BadRequest se o DOCUMENTO já estiver cadastrado")
    void erro_cpfcnpj_ja_adastrado_test() {
      Associado a = Associado.builder().documento(AssociadoMockTest.DOC_1).build();

      ErrorMatcher.toThrow(
          new BadRequestError("Associado já registrado"),
          () -> service.criarAssociado(a)
      );
    }
  }

  @Nested
  @Sql("classpath:sql/setup.sql")
  class AtualizarAssociado {
    @Test
    @DisplayName("deve atualizar um associado")
    void sucesso_test() {
      Optional<Associado> opt = repo.consultarPorCpfCnpj(AssociadoMockTest.DOC_1);

      assertTrue(opt.isPresent());

      Associado associado = opt.get();
      assertEquals(AssociadoMockTest.DOC_1, associado.getDocumento());

      final String cpf = "01234567890";
      associado.setDocumento(cpf);

      associado = service.atualizarAssociado(AssociadoMockTest.DOC_1, associado);
      assertEquals(cpf, associado.getDocumento());
    }

    @Test
    @DisplayName("deve retornar BadRequest se o CPF/CNPJ for inválido")
    void erro_cpfcnpj_invalido_test() {
      String documento = "123";
      Associado mock = AssociadoMockTest.getAssociado();
      mock.setDocumento(documento);

      ErrorMatcher.toThrow(
          new BadRequestError("CPF/CNPJ não aceito"),
          () -> service.atualizarAssociado(documento, mock)
      );
    }

    @Test
    @DisplayName("deve retornar BadRequest se o DOCUMENTO já estiver cadastrado")
    void erro_cpfcnpj_ja_cadastrado() {
      Associado mock = AssociadoMockTest.getAssociado();
      mock.setDocumento(AssociadoMockTest.DOC_1);

      ErrorMatcher.toThrow(
          new BadRequestError("CPF/CNPJ já registrado"),
          () -> service.atualizarAssociado(AssociadoMockTest.DOC_1, mock)
      );
    }
  }

  @Nested
  @Sql("classpath:sql/setup.sql")
  class ExcluirAssociado {
    @Test
    @DisplayName("deve excluir o associado")
    void sucesso_excluir_associado_test() {
      Optional<Associado> opt = repo.consultarPorCpfCnpj(AssociadoMockTest.DOC_1);
      assertTrue(opt.isPresent());

      service.excluir(opt.get().getDocumento());

      opt = repo.consultarPorCpfCnpj(opt.get().getDocumento());
      assertTrue(opt.isEmpty());
    }
  }

  @Nested
  @Sql("classpath:sql/setup.sql")
  class ConsultarAssociado {
    final int total = 4;

    @Test
    @DisplayName("deve listar todos os associados")
    void sucesso_listar_associados_test() {
      long count = repo.count();
      assertEquals(total, (int) count);

      List<Associado> l = service.listar();
      assertEquals((int) count, l.size());
    }

    @Test
    @DisplayName("deve consultar o associado por ID")
    void sucesso_listar_associado_por_id_test() {
      Associado a = service.consultarPorId(AssociadoMockTest.ID_1);
      assertNotNull(a);
    }

    @Test
    @DisplayName("deve lançar NotFound caso ID não encontrado")
    void erro_consultar_associado_por_id_test() {
      ErrorMatcher.toThrow(
          new NotFoundError(),
          () -> service.consultarPorId("batata")
      );
    }

    @Test
    @DisplayName("deve consultar o associado por cpf ou cnpj")
    void sucesso_consultar_associado_por_cpf_cnpj_test() {
      Associado a = service.consultarPorCpfCnpj(AssociadoMockTest.DOC_1);
      assertNotNull(a);
    }

    @Test
    @DisplayName("deve lançar NotFound caso CPF/CNPJ não encontrado")
    void error_consultar_por_cpf_cnpj_error() {
      ErrorMatcher.toThrow(
          new NotFoundError(),
          () -> service.consultarPorCpfCnpj("batata")
      );
    }
  }
}
