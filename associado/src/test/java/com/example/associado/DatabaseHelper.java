package com.example.associado;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseHelper {

	private Connection connection;
	private DatabaseConnection dbUnit;

	private static final String TESTING_PROPERTIE_FILE = "testing.properties";
	private static final String FLYWAY_LOCATION = "";

	private static final String DATASOURCE_PROPERTIE_FILE = "datasource.properties";
	private static final String DRIVER_PROP = "datasource.driver-class-name";
	private static final String URL_PROP = "datasource.url";
	private static final String DATABASE_NAME_PROP = "datasource.name";
	private static final String USERNAME_PROP = "datasource.username";
	private static final String PASSWORD_PROP = "datasource.password";
	private static final String SCHEMA_PROP = "datasource.schema";

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CustomDatasource implements Serializable {
		private String driverClassName;
		private String url;
		private String name;
		private String username;
		private String password;
		private String schema;
//		private String migrations;
	}

	public static DatabaseHelper getH2Instance() {
		try {
			Class.forName("org.h2.Driver");

			DatabaseHelper helper = new DatabaseHelper();

			helper.connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=ORACLE", "sa", "");
			helper.dbUnit = new DatabaseConnection(helper.connection);

			DatabaseConfig config = helper.dbUnit.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
			config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);

			return helper;
		} catch (Exception e) {
			throw new RuntimeException("Erro inicializando DBUnit", e);
		}
	}

	public static DatabaseHelper getInstance() {
		try {
			DatabaseHelper helper = new DatabaseHelper();
			Properties dsProperties = helper.loadProperties(DATASOURCE_PROPERTIE_FILE);

			return getInstance(
				CustomDatasource
					.builder()
					.driverClassName(dsProperties.getProperty(DRIVER_PROP))
					.url(dsProperties.getProperty(URL_PROP))
					.username(dsProperties.getProperty(USERNAME_PROP))
					.password(dsProperties.getProperty(PASSWORD_PROP))
					.schema(dsProperties.getProperty(SCHEMA_PROP))
					.build()
			);
		} catch (Exception e) {
			throw new RuntimeException("Erro inicializando DBUnit", e);
		}
	}

	public static DatabaseHelper getInstance(CustomDatasource cds) {
		try {
			DatabaseHelper helper = new DatabaseHelper();

			Class.forName(cds.getDriverClassName());
			Connection connection = DriverManager.getConnection(cds.getUrl(), cds.getUsername(), cds.getPassword());

			try (Statement stmt = connection.createStatement()) {
				String checkSchemaSql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = '" + cds.getSchema() + "'";
				boolean ok = stmt.executeQuery(checkSchemaSql).next();
				if (!ok) {
					log.info("Criando schema '{}'", cds.getSchema());
					int r = stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + cds.getSchema());
					connection.setSchema(cds.getSchema());
					log.info("Schema '{}' criado, resposta: '{}'", cds.getSchema(), r);
				}
			} catch (Exception e) {
				//
			}

			Properties testingProperties = helper.loadProperties(TESTING_PROPERTIE_FILE);

			runFlyWay(testingProperties);

			DatabaseConnection dbUnit = new DatabaseConnection(connection, cds.getSchema());
			DatabaseConfig config = dbUnit.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
			config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);

			helper.connection = connection;
			helper.dbUnit = dbUnit;
			return helper;
		} catch (Exception e) {
			throw new RuntimeException("Erro inicializando DBUnit", e);
		}
	}

	public DatabaseHelper execute(String dataset, DatabaseOperation operation) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dataset/" + dataset);
			if (is == null) {
				throw new FileNotFoundException("Arquivo dataset não encontrado: " + dataset);
			}

			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
			builder.setColumnSensing(true);
			IDataSet dataSet = builder.build(is);

			operation.execute(dbUnit, dataSet);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando DbUnit", e);
		}
		return this;
	}

	public DatabaseHelper executeSqlScript(String script) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(cleanPath(script));
			if (is == null) {
				throw new FileNotFoundException("Arquivo sql não encontrado: " + script);
			}

			List<String> comandos = this.loadCommandsFromSqlFile(is);

			for (String s : comandos) {
				if (s != null) {
					Statement st = connection.createStatement();
					log.info("Executando: " + s);
					st.execute(s);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Erro executando DbUnit", e);
		}
		return this;
	}

	public void close() {
		try {
			dbUnit.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Properties loadProperties(String file) throws IOException {
		Properties p = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(cleanPath(file));
		if (in == null) {
			throw new IOException("Não foi possível carregar o arquivo " + file + " durante o DatabaseHelper: stream nulo");
		}
		p.load(in);
		in.close();
		return p;
	}

	private static String cleanPath(String path) {
		return path != null && path.startsWith("/") ? path.substring(1) : path;
	}

	private static void runFlyWay(Properties properties) {
		String base = "flyway.";
		boolean isFlyWayEnabled = Boolean.parseBoolean(properties.getProperty(base + "enabled"));
		if (!isFlyWayEnabled) {
			return;
		}

		try {
			String locations = properties.getProperty(base + "locations");
			Resource resource = new ClassPathResource(locations);
			if (!resource.exists()) {
				throw new RuntimeException("Diretório '" + locations + "' não encontrado!");
			}

			log.info("Configurando & Executando Flyway");
			Flyway flyway = Flyway
				.configure()
				.dataSource(
					properties.getProperty(base + "url"),
					properties.getProperty(base + "user"),
					properties.getProperty(base + "password")
				)
				.schemas("test")
				.validateOnMigrate(Boolean.parseBoolean(properties.getProperty(base + "validate-on-migrate")))
				.failOnMissingLocations(Boolean.parseBoolean(properties.getProperty(base + "fail-on-missing-locations")))
				.table(properties.getProperty(base + "table"))
				.sqlMigrationPrefix(properties.getProperty(base + "sql-migration-separator"))
				.sqlMigrationSeparator(properties.getProperty(base + "sql-migration-prefix"))
				.encoding(properties.getProperty(base + "encoding"))
				.connectRetries(Integer.parseInt(properties.getProperty(base + "connect-retries")))
				.locations("filesystem:" + resource.getFile().getAbsolutePath())
				.load();

			MigrateResult result = flyway.migrate();
			log.info("Flyway executado");
		} catch (Exception e) {
			throw new RuntimeException("Erro executando o FlyWay");
		}
	}

	private List<String> loadCommandsFromSqlFile(InputStream is) {
		List<String> linhas = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		StringBuilder sbFunction = new StringBuilder();

		try (InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {

			String line;
			boolean isFunction = false;
			while ((line = br.readLine()) != null) {
				// comentario
				if (line.startsWith("--")) {
					continue;
				}

				//TODO: sintaxe de procedure Oracle
				if (line.startsWith("CREATE FUNCTION")) {
					isFunction = true;
					sbFunction.append(line);
					sbFunction.append("\n");
					continue;
				}

				if (line.startsWith("END;;")) {
					isFunction = false;
					sbFunction.append(line.replace(";;", ""));
					linhas.add(sbFunction.toString());
					sbFunction = new StringBuilder();
					continue;
				}

				if (isFunction) {
					sb.append(" ");
					sbFunction.append(line);
					sbFunction.append("\n");
				} else {
					//fim do comando
					if (line.trim().endsWith(";")) {
						sb.append(line);
						linhas.add(sb.toString());
						sb = new StringBuilder();
					} else {
						sb.append(line);
						sb.append(" ");
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		//silent close resources
		return linhas;
	}
}
