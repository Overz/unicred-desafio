package com.example.common.events;

public class Events {
	private Events() {
	}

	public static class Associado {
		private Associado() {
		}

		/**
		 * Mediador, decide o que vai ser feito com a mensagem
		 */
		public static final String ASSOCIADO_EXCHANGE = "fanout:associado";
		public static final String ASSOCIADO_QUEUE_NAME = "queue:associado";
		public static final String ASSOCIADO_EXCLUIR_QUEUE_NAME = "queue:associado:delete";
		public static final String ASSOCIADO_ATUALIZADO_QUEUE_NAME = "queue:associado:updated";
		public static final String ASSOCIADO_CRIADO_ROUTING_KEY = "routing:associado:new";
		public static final String ASSOCIADO_EXCLUIR_ROUTING_KEY = "routing:associado:delete";
		public static final String ASSOCIADO_ATUALIZADO_ROUTING_KEY = "routing:associado:updated";
	}

	public static class Boleto {
		private Boleto() {
		}

		public static final String BOLETO_EXCHANGE = "fanout:boleto";
		public static final String BOLETO_QUEUE_NAME = "queue:boleto";
		public static final String BOLETO_CRIADO_ROUTING_KEY = "routing:boleto:new";
		public static final String BOLETO_EXCLUIR_ROUTING_KEY = "routing:boleto:delete";
		public static final String BOLETO_ATUALIZADO_ROUTING_KEY = "routing:boleto:updated";
	}

	public static class Arquivo {
		private Arquivo() {
		}

		public static final String ARQUIVO_EXCHANGE = "fanout:arquivo";
		public static final String ARQUIVO_QUEUE_NAME = "queue:arquivo";
		public static final String ARQUIVO_CRIADO_ROUTING_KEY = "routing:arquivo:new";
		public static final String ARQUIVO_PROCESSADO_ROUTING_KEY = "routing:arquivo:finished";
	}
}
