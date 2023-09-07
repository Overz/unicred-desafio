package com.example.boleto.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.common.events.Events.Associado.ASSOCIADO_EXCHANGE;
import static com.example.common.events.Events.Associado.ASSOCIADO_QUEUE_NAME;
import static com.example.common.events.Events.Boleto.BOLETO_EXCHANGE;
import static com.example.common.events.Events.Boleto.BOLETO_QUEUE_NAME;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue associadoQueue() {
		return new Queue(ASSOCIADO_QUEUE_NAME);
	}

	@Bean
	public FanoutExchange associadoFanoutExchange() {
		return new FanoutExchange(ASSOCIADO_EXCHANGE);
	}

	@Bean
	public Binding associadoCriadoBinding() {
		return BindingBuilder.bind(associadoQueue()).to(associadoFanoutExchange());
	}

	@Bean
	public Queue boletoQueue() {
		return new Queue(BOLETO_QUEUE_NAME);
	}

	@Bean
	public FanoutExchange boletoFanoutExchange() {
		return new FanoutExchange(BOLETO_EXCHANGE);
	}

	@Bean
	public Binding boletoCriadoBinding() {
		return BindingBuilder.bind(boletoQueue()).to(boletoFanoutExchange());
	}
}
