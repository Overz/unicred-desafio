package com.example.common.mappers;


import com.example.common.errors.MapperError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapperUtils {

	private MapperUtils() {
	}

	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public static synchronized <T> T fromJson(String stringJson, Class<T> target) {
		try {
			return objectMapper.readValue(stringJson, target);
		} catch (Exception e) {
			throw new MapperError("Erro ao serializar JSON para Objeto '{}'", target, e);
		}
	}

	public static synchronized <T> String toJson(T source) {
		try {
			return objectMapper.writeValueAsString(source);
		} catch (Exception e) {
			throw new MapperError("Erro ao serializar objeto para JSON '{}'", source.getClass(), e);
		}
	}

	public static synchronized <T> T convert(Object input, Class<T> cls) {
		return fromJson(toJson(input), cls);
	}
}
