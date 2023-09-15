package com.example.associado.utils;

import org.junit.platform.commons.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

public abstract class PropertiesTest {

	public static final String DEFAULT_FILE = "application.yaml";
	public static final String CLASSPATH_DEFAULT_FILE = "classpath:" + DEFAULT_FILE;
	public static final String LOCAL_FILE = "application-local.yaml";
	public static final String CLASSPATH_LOCAL_FILE = "classpath:" + LOCAL_FILE;
	public static final String INTEGRATION_FILE = "application-local.yaml";
	public static final String CLASSPATH_INTEGRAION_FILE = "classpath:" + INTEGRATION_FILE;

	private static final Properties properties = new Properties();

	static {
		load();
	}

	private static void load() {
		load(DEFAULT_FILE);
	}

	public static void load(String path) {
		try {
			path = path.startsWith("/") ? path.substring(1) : path;
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

			if (path.endsWith(".yaml") || path.endsWith(".yml")) {
				Map<String, Object> source = new Yaml().load(is);
				properties.putAll(getFlattenedMap(source));
			} else {
				properties.load(is);
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro carregando propriedades do arquivo '" + path + "'");
		}
	}

	private static Map<String, Object> getFlattenedMap(Map<String, Object> source) {
		Map<String, Object> result = new LinkedHashMap<>();
		buildFlattenedMap(result, source, null);
		return result;
	}

	private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
		source.forEach((key, value) -> {
			if (!StringUtils.isBlank(path))
				key = path + (key.startsWith("[") ? key : '.' + key);
			if (value instanceof Map) {
				buildFlattenedMap(result, (Map<String, Object>) value, key);
			} else if (value instanceof Collection) {
				int count = 0;
				for (Object object : (Collection<?>) value)
					buildFlattenedMap(result, Collections.singletonMap("[" + (count++) + "]", object), key);
			} else {
				result.put(key, value != null ? "" + value : "");
			}
		});
	}

	public static String getString(String p) {
		return properties.getProperty(p);
	}

	public static Integer getInteger(String p) {
		return Integer.parseInt(properties.getProperty(p));
	}

	public static Boolean getBoolean(String p) {
		return Boolean.parseBoolean(properties.getProperty(p));
	}

	public static Long getLong(String p) {
		return Long.parseLong(properties.getProperty(p));
	}
}
