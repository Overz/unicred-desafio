package com.example.associado;

import com.example.common.constants.ProfilesConstants;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.nio.file.Paths;

@Slf4j
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
		String project = Paths.get(root, "associado").toAbsolutePath().toString();
		log.info("Procurando .env no diretorio '{}'", project);
		Dotenv.configure().directory(project).systemProperties().ignoreIfMissing().load();
	}
}
