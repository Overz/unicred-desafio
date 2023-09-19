package com.example.associado.controllers;


import com.example.associado.SetupTests;
import com.example.associado.events.AssociadoEventPublisher;
import com.example.associado.mocks.AssociadoMockTest;
import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static com.example.common.mappers.MapperUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = {AssociadoController.class},
    excludeAutoConfiguration = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class, FlywayAutoConfiguration.class}
)
@ExtendWith({MockitoExtension.class})
@AutoConfigureMockMvc
public class AssociadoControllerIntegrationTest extends Assertions {

  @MockBean
  AssociadoService service;

  @MockBean
  AssociadoEventPublisher publisher;

  @Autowired
  MockMvc mvc;

	@BeforeAll
	static void init() {
		SetupTests.setupProperties();
	}

  @Nested
  class CriarAssociado {
    Associado mock;

    MockHttpServletRequestBuilder toRequest(Associado a) {
      return post("/associado").contentType(MediaType.APPLICATION_JSON)
          .content(toJson(a.toDTO()));
    }

    @BeforeEach
    void setup() {
      mock = AssociadoMockTest.getAssociado();
    }

    @Test
    @DisplayName("deve criar um associado")
    void sucesso_criar_associado_test() throws Exception {
      Mockito.doReturn(mock).when(service).criarAssociado(Mockito.any(Associado.class));

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isCreated(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath("id", Matchers.equalTo(mock.getUuid())),
              jsonPath("documento", Matchers.equalTo(mock.getDocumento())),
              jsonPath("nome", Matchers.equalTo(mock.getNome())),
              jsonPath("tipoPessoa", Matchers.equalTo(mock.getTipo_pessoa()))
          );
    }

    @Test
    @DisplayName("deve retornar 400 caso errors em DOCUMENTO")
    void erro_criar_associado_faltando_documento_test() throws Exception {
      // NULO/VAZIO
      mock.setDocumento(null);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.documento",
                  Matchers.oneOf(
                      "Documento não deve ser nulo",
                      "Documento não deve ser vazio"
                  )
              )
          );

      // PATTERN/REGEX
      mock.setDocumento("123");

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.documento",
                  Matchers.equalToIgnoringCase("O Documento deve ser o CPF/CNPJ sem formatação")
              )
          );
    }

    @Test
    @DisplayName("deve retornar 400 caso faltando errors em TIPO PESSOA")
    void erro_criar_associado_faltando_tipo_pessoa_test() throws Exception {
      // NULO/VAZIO
      mock.setTipo_pessoa(null);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.tipoPessoa",
                  Matchers.oneOf(
                      "Tipo Pessoa não deve ser nulo",
                      "Tipo Pessoa não deve ser vazio"
                  )
              )
          );

      // PATTER/REGEX
      mock.setTipo_pessoa("batata");

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.tipoPessoa",
                  Matchers.equalToIgnoringCase("O tipo da pessoa deve ser 'PF' ou 'PJ'")
              )
          );
    }

    @Test
    @DisplayName("deve retornar 400 caso faltando NOME")
    void erro_criar_associado_faltando_nome_test() throws Exception {
      // NULO/VAZIO
      mock.setNome(null);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.nome",
                  Matchers.oneOf(
                      "Nome não deve ser nulo",
                      "Nome não deve ser vazio"
                  )
              )
          );

      // PATTERN/REGEX
      String repeatedString = new String(new char[100]).replace(Character.MIN_VALUE, 'X');
      mock.setNome(repeatedString);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.nome",
                  Matchers.equalToIgnoringCase("Limite de Caracteres do nome atingido")
              )
          );
    }
  }

  @Nested
  class AtualizarAssociado {

    Associado mock;

    final MockHttpServletRequestBuilder toRequest(Associado a) {
      return put("/associado/{cpfcnpj}", "12345")
          .contentType(MediaType.APPLICATION_JSON).content(toJson(a.toDTO()));
    }

    @BeforeEach
    void setup() {
      mock = AssociadoMockTest.getAssociado();
    }

    @Test
    @DisplayName("deve atualizar um associado")
    void sucesso_atualizar_associado_test() throws Exception {
      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isNoContent()
          ).andReturn();

      Mockito.verify(service, Mockito.times(1))
          .atualizarAssociado(Mockito.anyString(), Mockito.any(Associado.class));
    }

    @Test
    @DisplayName("deve retornar 400 caso errors em DOCUMENTO")
    void erro_atualizar_associado_faltando_documento_test() throws Exception {
      // NULO/VAZIO
      mock.setDocumento(null);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.documento",
                  Matchers.oneOf(
                      "Documento não deve ser nulo",
                      "Documento não deve ser vazio"
                  )
              )
          );

      // PATTERN/REGEX
      mock.setDocumento("123");

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.documento",
                  Matchers.equalToIgnoringCase("O Documento deve ser o CPF/CNPJ sem formatação")
              )
          );
    }

    @Test
    @DisplayName("deve retornar 400 caso faltando errors em TIPO PESSOA")
    void erro_atualizar_associado_faltando_tipo_pessoa_test() throws Exception {
      // NULO/VAZIO
      mock.setTipo_pessoa(null);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.tipoPessoa",
                  Matchers.oneOf(
                      "Tipo Pessoa não deve ser nulo",
                      "Tipo Pessoa não deve ser vazio"
                  )
              )
          );

      // PATTER/REGEX
      mock.setTipo_pessoa("batata");

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.tipoPessoa",
                  Matchers.equalToIgnoringCase("O tipo da pessoa deve ser 'PF' ou 'PJ'")
              )
          );
    }

    @Test
    @DisplayName("deve retornar 400 caso faltando NOME")
    void erro_atualizar_associado_faltando_nome_test() throws Exception {
      // NULO/VAZIO
      mock.setNome(null);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.nome",
                  Matchers.oneOf(
                      "Nome não deve ser nulo",
                      "Nome não deve ser vazio"
                  )
              )
          );

      // PATTERN/REGEX
      String repeatedString = new String(new char[100]).replace(Character.MIN_VALUE, 'X');
      mock.setNome(repeatedString);

      mvc.perform(toRequest(mock))
          .andExpectAll(
              status().isBadRequest(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath(
                  "errors.nome",
                  Matchers.equalToIgnoringCase("Limite de Caracteres do nome atingido")
              )
          );
    }
  }

  @Nested
  class ExcluirAssociado {
    Associado mock;

    final MockHttpServletRequestBuilder toRequest(Associado a) {
      return delete("/associado/{cpfcnpj}", "12345")
          .content(toJson(a.toDTO()));
    }

    @BeforeEach
    void setup() {
      mock = AssociadoMockTest.getAssociado();
      Mockito.doReturn(mock).when(service).consultarPorCpfCnpj(Mockito.anyString());
    }

    @Test
    @DisplayName("deve excluir um associado e emitir um evento pelo message broker")
    void sucesso_excluir_associado_test() {

    }
  }

  @Nested
  class ListarAssociado {
    final List<Associado> list = AssociadoMockTest.getAssociadoList();
    final Associado mock = AssociadoMockTest.getAssociado();

    @Test
    @DisplayName("deve retornar o associado por CPF/CPNJ")
    void sucesso_consultar_por_cpf_cnpj_test() throws Exception {
      Mockito.doReturn(mock).when(service).consultarPorCpfCnpj(Mockito.anyString());

      mvc.perform(get("/associado/{cpfcnpj}", mock.getDocumento()))
          .andExpectAll(
              status().isOk(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath("id", Matchers.equalTo(mock.getUuid())),
              jsonPath("documento", Matchers.equalTo(mock.getDocumento())),
              jsonPath("nome", Matchers.equalTo(mock.getNome())),
              jsonPath("tipoPessoa", Matchers.equalTo(mock.getTipo_pessoa()))
          );
    }

    @Test
    @DisplayName("deve listar todos os associados")
    void sucesso_listar_test() throws Exception {
      Mockito.doReturn(list).when(service).listar();

      Associado content = list.get(0);
      mvc.perform(get("/associado"))
          .andExpectAll(
              status().isOk(),
              content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
              jsonPath("$", Matchers.hasSize(1)),
              jsonPath("$[0].id", Matchers.equalTo(content.getUuid())),
              jsonPath("$[0].documento", Matchers.equalTo(content.getDocumento())),
              jsonPath("$[0].tipoPessoa", Matchers.equalTo(content.getTipo_pessoa())),
              jsonPath("$[0].nome", Matchers.equalTo(content.getNome()))
          );
    }
  }
}
