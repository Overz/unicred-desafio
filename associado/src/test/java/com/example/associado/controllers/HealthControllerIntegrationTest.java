package com.example.associado.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    properties = {
        "spring.flyway.enabled=false"
    },
    controllers = {HealthController.class}
)
@AutoConfigureMockMvc
public class HealthControllerIntegrationTest {

  @Value("${spring.application.name}")
  String appName;

  @Value("#{environment.getActiveProfiles()}")
  String[] profiles;

  @Autowired
  MockMvc mvc;

  @Test
  @DisplayName("Deve retornar dados para checapsgem se o sistema esta funcionando")
  void healthIntegrationTest() throws Exception {
    mvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpectAll(
            content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
            jsonPath("name", Matchers.equalToIgnoringCase(appName)),
            jsonPath("date", Matchers.notNullValue()),
            jsonPath("profiles", Matchers.contains(profiles))
        );
  }
}
