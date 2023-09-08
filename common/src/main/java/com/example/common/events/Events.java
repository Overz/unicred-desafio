package com.example.common.events;

public class Events {
	private Events() {
	}

	/**
	 * ASSOCIADO
	 */
	public static final String ASSOCIADO_EXCHANGE = "direct:associado";
	public static final String ASSOCIADO_EXCLUIR_QUEUE = "ASSOCIADO_EXCLUIR_QUEUE";
	public static final String ASSOCIADO_EXCLUIR_ROUTING_KEY = "ASSOCIADO_EXCLUIR_ROUTING_KEY";

	/**
	 * BOLETO
	 */
	public static final String BOLETO_EXCHANGE = "direct:boleto";
	public static final String BOLETO_CONSULTAR_ASSOCIADO_QUEUE = "BOLETO_CONSULTAR_ASSOCIADO_QUEUE";
	public static final String BOLETO_CONSULTAR_ASSOCIADO_ROUTING_KEY = "BOLETO_CONSULTAR_ASSOCIADO_ROUTING_KEY";
	public static final String BOLETO_CONSULTAR_ASSOCIADO_POR_ID_SUBJECT = "BOLETO_CONSULTAR_ASSOCIADO_POR_ID_SUBJECT";
	public static final String BOLETO_CONSULTAR_ASSOCIADO_POR_CPFCNPJ_SUBJECT = "BOLETO_CONSULTAR_ASSOCIADO_POR_CPFCNPJ_SUBJECT";

	/**
	 * ARQUIVO
	 */
	public static final String ARQUIVO_EXCHANGE = "direct:arquivo";
	public static final String ARQUIVO_PROCESSADO_QUEUE = "ARQUIVO_PROCESSADO_QUEUE";
	public static final String ARQUIVO_ENVIA_DADOS_PARA_BOLETO_ROUTING_KEY = "ARQUIVO_ENVIA_DADOS_PARA_BOLETO_ROUTING_KEY";
}
