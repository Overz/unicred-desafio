package com.example.common.events;

public interface Publisher {
	void send(Object model) throws RuntimeException;

	void send(String routingKey, Object model) throws RuntimeException;

	void send(String routingKey, String message) throws RuntimeException;

	void send(String exchange, String routingKey, String message) throws RuntimeException;

	void send(String exchange, String routingKey, Object model) throws RuntimeException;

	MessageStreaming exchange(Object model) throws RuntimeException;

	MessageStreaming exchange(String routingKey, Object model) throws RuntimeException;

	MessageStreaming exchange(String routingKey, String message) throws RuntimeException;

	MessageStreaming exchange(String exchange, String routingKey, String message) throws RuntimeException;

	MessageStreaming exchange(String exchange, String routingKey, Object model) throws RuntimeException;
}
