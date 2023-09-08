package com.example.boleto.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.common.events.Events.*;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue associadoExcluirQueue() {
		return new Queue(ASSOCIADO_EXCLUIR_QUEUE);
	}

	@Bean
	public DirectExchange associadoDirectExchange() {
		return new DirectExchange(ASSOCIADO_EXCHANGE);
	}

	@Bean
	public Binding associadoExcluirBinding() {
		return BindingBuilder
			.bind(associadoExcluirQueue())
			.to(associadoDirectExchange())
			.with(ASSOCIADO_EXCLUIR_ROUTING_KEY);
	}

	@Bean
	public DirectExchange boletoDirectExchange() {
		return new DirectExchange(BOLETO_EXCHANGE);
	}

	@Bean
	public Queue boletoConsultarAssociadoQueue() {
		return new Queue(BOLETO_CONSULTAR_ASSOCIADO_QUEUE);
	}

	@Bean
	public Binding boletoConsultarAssociadoBinding() {
		return BindingBuilder
			.bind(boletoConsultarAssociadoQueue())
			.to(boletoDirectExchange())
			.with(BOLETO_CONSULTAR_ASSOCIADO_ROUTING_KEY);
	}

	@Bean
	public DirectExchange arquivoDirectExchange() {
		return new DirectExchange(ARQUIVO_EXCHANGE);
	}

	@Bean
	public Queue arquivoProcessadoQueue() {
		return new Queue(ARQUIVO_PROCESSADO_QUEUE);
	}

	@Bean
	public Binding arquivoProcessadoBinding() {
		return BindingBuilder
			.bind(arquivoProcessadoQueue())
			.to(arquivoDirectExchange())
			.with(ARQUIVO_ENVIA_DADOS_PARA_BOLETO_ROUTING_KEY);
	}
}
