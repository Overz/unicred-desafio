package com.example.associado;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@SpringBootApplication(
	exclude = {
		RabbitAutoConfiguration.class
	}
)
public class Main {

	public static void main(String[] args) {
		Dotenv.configure().systemProperties().ignoreIfMissing().load();
		SpringApplication.run(Main.class, args);
	}
}
