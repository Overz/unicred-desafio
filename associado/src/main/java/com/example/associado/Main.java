package com.example.associado;

import com.example.common.constants.ProfilesConstants;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@SpringBootApplication
@ConfigurationPropertiesScan
public class Main {

	public static void main(String[] args) {
		System.setProperty("START_TIME", "" + System.currentTimeMillis());

		env();
		SpringApplication.run(Main.class, args);
	}

	private static void env() {
		String[] profiles = new String[]{System.getProperty("SPRING_PROFILES_ACTIVE")};
		if (!ArrayUtils.contains(profiles, ProfilesConstants.DEV)) {
			return;
		}

		String root = System.getProperty("user.dir");
		Path path = Paths.get(root, "associado");

		String project = path.toAbsolutePath().toString();
		log.info("Procurando .env no diretorio '{}'", project);

		if (!Files.exists(path)) {
			String dir = Paths.get(root).toAbsolutePath().toString();
			log.warn("Diretório '{}' não encontrado, alterando para '{}'", project, dir);
			project = dir;
		}

		log.info("Carregando .env no diretorio '{}'", project);
		Dotenv.configure().directory(project).systemProperties().ignoreIfMissing().load();
	}
}
