package com.example.associado.controllers;

import com.example.common.constants.ProfilesConstants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({ProfilesConstants.INTEGRATION_TEST})
@TestPropertySource({"classpath:integration-test.properties"})
public class HealthControllerIntegrationTest {

	@Value("${spring.application.name}")
	String appName;

	@Value("#{environment.getActiveProfiles()}")
	String[] profiles;

	@Autowired
	MockMvc mvc;

	@Test
	@DisplayName("batata")
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