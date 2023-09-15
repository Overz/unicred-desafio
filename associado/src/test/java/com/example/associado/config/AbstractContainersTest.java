package com.example.associado.config;

import com.example.common.events.Events;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractContainersTest {

  @Container
  public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
  @Container
  public static final RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3.12-management");

  static {
    final String TZ = "America/Sao_Paulo";

    postgres
        .withReuse(true)
        .withEnv("TZ", TZ)
        .withEnv("POSTGRES_TZDATA", "/usr/share/zoneinfo/America/Sao_Paulo")
        .withStartupTimeoutSeconds(2)
        .withConnectTimeoutSeconds(4)
    ;

    rabbitmq
        .withReuse(true)
        .withEnv("TZ", TZ)
        .withQueue(Events.ASSOCIADO_EXCLUIR_QUEUE)
        .withQueue(Events.BOLETO_CONSULTAR_ASSOCIADO_QUEUE)
        .withExchange(Events.ASSOCIADO_EXCHANGE, "direct")
        .withExchange(Events.BOLETO_EXCHANGE, "direct")
        .withBinding("", "", null, "", "")
    ;
  }

  @BeforeAll
  static void init() {
		System.setProperty("START_TIME", "" + System.currentTimeMillis());
    runContainers();
  }

  @AfterAll
  static void down() {
    stopContainers();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    // DATASOURCE
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.username", postgres::getUsername);

    // MESSAGE BROKER
    registry.add("spring.rabbitmq.host", rabbitmq::getHost);
    registry.add("spring.rabbitmq.username", rabbitmq::getAdminUsername);
    registry.add("spring.rabbitmq.password", rabbitmq::getAdminPassword);
    registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
    registry.add("spring.rabbitmq.template.reply-timeout", () -> 5);
  }

  public static void runContainers() {
    postgres.start();
    rabbitmq.start();
  }

  public static void stopContainers() {
    postgres.stop();
    rabbitmq.stop();
  }

//	public static class Init implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//		@Override
//		public void initialize(ConfigurableApplicationContext ctx) {
//			TestPropertyValues.of(
//				DATASOURCE_URL + "=" + postgres.getJdbcUrl(),
//				DATASOURCE_USERNAME + "=" + postgres.getUsername(),
//				DATASOURCE_PASSWORD + "=" + postgres.getPassword()
//			).applyTo(ctx.getEnvironment());
//		}
//	}
}
