package com.example.boleto;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;

@SpringBootApplication()
public class Main {

	public static void main(String[] args) {
		env();
		SpringApplication.run(Main.class, args);
	}

	private static void env() {
		String root = System.getProperty("user.dir");
		String project = Paths.get(root, "boleto").toAbsolutePath().toString();
		Dotenv.configure().directory(project).systemProperties().ignoreIfMissing().load();
	}
}
