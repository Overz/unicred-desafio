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
import org.flywaydb.core.api.output.MigrateOutput;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.example.associado.constants.ConstantsUtilsTest.APPLICATION_TESTING_PROPERTIE_FILE;
import static com.example.associado.constants.DatasourceConstantsTest.*;
import static com.example.associado.constants.FlyWayConstantsTest.*;

@Slf4j
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseHelperTest {

	private Connection connection;
	private DatabaseConnection dbUnit;

	private static final Properties properties = loadProperties(APPLICATION_TESTING_PROPERTIE_FILE);

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
	}

	public static DatabaseHelperTest getH2Instance() {
		try {
			Class.forName("org.h2.Driver");

			DatabaseHelperTest helper = new DatabaseHelperTest();

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

	public static DatabaseHelperTest getInstance() {
		try {
			return getInstance(
				CustomDatasource
					.builder()
					.driverClassName(properties.getProperty(DATASOURCE_DRIVER_CLASS_NAME))
					.url(properties.getProperty(DATASOURCE_URL))
					.name(properties.getProperty(DATASOURCE_DATABASE_NAME))
					.username(properties.getProperty(DATASOURCE_USERNAME))
					.password(properties.getProperty(DATASOURCE_PASSWORD))
					.schema(properties.getProperty(DATASOURCE_SCHEMA))
					.build()
			);
		} catch (Exception e) {
			throw new RuntimeException("Erro inicializando DBUnit", e);
		}
	}

	public static DatabaseHelperTest getInstance(CustomDatasource cds) {
		try {

			Class.forName(cds.getDriverClassName());

			Connection connection = DriverManager.getConnection(cds.getUrl(), cds.getUsername(), cds.getPassword());
			createDatabaseSchema(connection, cds.getSchema());

			runFlyWay();

			DatabaseConnection dbUnit = new DatabaseConnection(connection, cds.getSchema());
			DatabaseConfig config = dbUnit.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
			config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);

			return new DatabaseHelperTest(connection, dbUnit);
		} catch (Exception e) {
			throw new RuntimeException("Erro inicializando DBUnit", e);
		}
	}

	public DatabaseHelperTest execute(String dataset, DatabaseOperation operation) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataset);
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

	public DatabaseHelperTest executeSqlScript(String script) {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(cleanPath(script));
			if (is == null) {
				throw new FileNotFoundException("Arquivo sql não encontrado: " + script);
			}

			List<String> comandos = loadCommandsFromSqlFile(is);

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

	private static void createDatabaseSchema(Connection connection, String schema) {
		try (Statement stmt = connection.createStatement()) {
			String checkSchemaSql = """
				SELECT schema_name FROM information_schema.schemata
				WHERE schema_name = '""" + schema + "'";

			boolean ok = stmt.executeQuery(checkSchemaSql).next();
			if (!ok) {
				log.info("Criando schema '{}'", schema);
				int r = stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + schema);
				connection.setSchema(schema);
				log.info("Schema '{}' criado, resposta: '{}'", schema, r);
			}
		} catch (Exception e) {
			log.error("Erro criando o Schema '{}'", schema, e);
		}
	}

	public void close() {
		try {
			dbUnit.close();
			connection.close();
		} catch (SQLException e) {
			log.error("Erro fechando DBUnit e Connection", e);
		}
	}

	private static Properties loadProperties(String file) {
		try {
			Properties p = new Properties();
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(cleanPath(file));
			if (in == null) {
				throw new IOException("Não foi possível carregar o arquivo " + file + " durante o DatabaseHelper: stream nulo");
			}
			p.load(in);
			in.close();
			return p;
		} catch (Exception e) {
			throw new RuntimeException("Erro carregando propertie '" + file + "'");
		}
	}

	private static String cleanPath(String path) {
		return path != null && path.startsWith("/") ? path.substring(1) : path;
	}

	private static void runFlyWay() {
		try {
			boolean isFlyWayEnabled = Boolean.parseBoolean(properties.getProperty(FLYWAY_ENABLED));
			if (!isFlyWayEnabled) {
				return;
			}

			log.info("Configurando & Executando Flyway");

			Flyway flyway = Flyway
				.configure()
				.dataSource(
					properties.getProperty(FLYWAY_URL),
					properties.getProperty(FLYWAY_USER),
					properties.getProperty(FLYWAY_PASSWORD)
				)
				.validateOnMigrate(Boolean.parseBoolean(properties.getProperty(FLYWAY_VALIDATE_ON_MIGRATE)))
				.failOnMissingLocations(Boolean.parseBoolean(properties.getProperty(FLYWAY_FAIL_ON_MISSING_LOCATION)))
				.table(properties.getProperty(FLYWAY_TABLE))
				.sqlMigrationPrefix(properties.getProperty(FLYWAY_SQL_MIGRATION_PREFIX))
				.sqlMigrationSeparator(properties.getProperty(FLYWAY_SQL_MIGRATION_SEPARATOR))
				.encoding(properties.getProperty(FLYWAY_ENCODING))
				.connectRetries(Integer.parseInt(properties.getProperty(FLYWAY_CONNECT_RETRIES)))
				.connectRetriesInterval(Integer.parseInt(properties.getProperty(FLYWAY_CONNECT_RETRIES_INTERVAL)))
				.locations("filesystem:" + getResourcePath(properties.getProperty(FLYWAY_LOCATIONS)))
				.load();

			MigrateResult result = flyway.migrate();
			log.info("Flyway executado");

			for (MigrateOutput migration : result.migrations) {
				String version = migration.version;
				String name = migration.description;
				String ext = migration.type.toLowerCase();
				log.info(version + "-" + name + "." + ext);
			}

		} catch (Exception e) {
			throw new RuntimeException("Erro executando o FlyWay");
		}
	}

	private static String getResourcePath(String path) throws IOException {
		Resource resource = new ClassPathResource(path);
		if (!resource.exists()) {
			throw new RuntimeException("Diretório '" + path + "' não encontrado!");
		}

		return resource.getFile().getAbsolutePath();
	}

	private static List<String> loadCommandsFromSqlFile(InputStream is) {
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
			log.error("Erro lendo linhas do script SQL", e);
		}

		return linhas;
	}
}
