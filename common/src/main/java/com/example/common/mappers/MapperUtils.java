package com.example.common.mappers;


import com.example.common.errors.MapperError;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtils {

  private MapperUtils() {
  }

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
}
