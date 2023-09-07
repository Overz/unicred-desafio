package com.example.common.events;

public interface Producer<T> {
	void send(String routingKey, Object model) throws RuntimeException;

	MessageStreaming<T> exchange(String routingKey, Object model) throws RuntimeException;
}
