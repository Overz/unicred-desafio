package com.example.common.events;

public interface Publisher {
  void send(Object model) throws RuntimeException;

  void send(String routingKey, Object model) throws RuntimeException;

  void send(String routingKey, String message) throws RuntimeException;

  void send(String exchange, String routingKey, String message) throws RuntimeException;

  void send(String exchange, String routingKey, Object model) throws RuntimeException;

  <T> MessageStreaming<T> exchange(Object model) throws RuntimeException;

  <T> MessageStreaming<T> exchange(String routingKey, Object model) throws RuntimeException;

  <T> MessageStreaming<T> exchange(String routingKey, String message) throws RuntimeException;

  <T> MessageStreaming<T> exchange(String exchange, String routingKey, String message) throws RuntimeException;

  <T> MessageStreaming<T> exchange(String exchange, String routingKey, Object model) throws RuntimeException;
}
