package com.example.associado.config;

import com.example.common.mappers.MapperUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.common.events.Events.*;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter(MapperUtils.getMaper());
	}

	@Bean
	public DirectExchange associadoDirectExchange() {
		return new DirectExchange(ASSOCIADO_EXCHANGE);
	}

	@Bean
	public Queue associadoExcluirQueue() {
		return new Queue(ASSOCIADO_EXCLUIR_QUEUE);
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
	public Binding boletoBinding() {
		return BindingBuilder
			.bind(boletoConsultarAssociadoQueue())
			.to(boletoDirectExchange())
			.with(BOLETO_CONSULTAR_ASSOCIADO_ROUTING_KEY);
	}
}
