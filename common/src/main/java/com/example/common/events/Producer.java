package com.example.common.events;

public interface Producer {
  void send(String id, String message) throws RuntimeException;

  void send(String id, Object model) throws RuntimeException;

  Object sendAndReceived(String id, String message) throws RuntimeException;

  Object sendAndReceived(String id, Object model) throws RuntimeException;
}
