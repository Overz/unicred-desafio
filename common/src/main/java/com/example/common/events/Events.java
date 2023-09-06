package com.example.common.events;

public class Events {
  private Events() {
  }

  public static class Associado {
    private Associado() {
    }

    public static final String ASSOCIADO_CRIADO_QUEUE_NAME = "queue:associado:new";
    public static final String ASSOCIADO_EXCLUIR_QUEUE_NAME = "queue:associado:delete";
    public static final String ASSOCIADO_ATUALIZADO_QUEUE_NAME = "queue:associado:updated";
    /**
     * Mediador, decide o que vai ser feito com a mensagem
     */
    public static final String ASSOCIADO_PUBSUB_TOPIC = "associado-topic";
  }
}
