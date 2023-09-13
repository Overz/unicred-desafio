package com.example.associado.config;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.example.associado.utils.PropertiesTest.getString;
import static com.example.associado.constants.DatasourceConstantsTest.*;

@NoArgsConstructor
public class PGSQLContainerTest extends Assertions {
	private static final String DOCKER_IMG = "postgres:latest";
	private static final String INIT_SQL = "";
	private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DOCKER_IMG);

	static {
		container
			.withReuse(true)
			.withDatabaseName(getString(DATASOURCE_URL))
			.withUsername(getString(DATASOURCE_USERNAME))
			.withPassword(getString(DATASOURCE_PASSWORD))
			.withInitScript(INIT_SQL)
			.withConnectTimeoutSeconds(3)
		;
	}

	public static void runContainer() {
		container.start();
	}

	public static void stopContainer() {
		container.stop();
	}

	public static class Init implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext ctx) {
			TestPropertyValues.of(
				DATASOURCE_URL + "=" + container.getJdbcUrl(),
				DATASOURCE_USERNAME + "=" + container.getUsername(),
				DATASOURCE_PASSWORD + "=" + container.getPassword()
			).applyTo(ctx.getEnvironment());
		}
	}
}
