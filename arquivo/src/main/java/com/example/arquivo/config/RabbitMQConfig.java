package com.example.arquivo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.common.events.Events.*;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(ARQUIVO_EXCHANGE);
	}

	@Bean
	public Queue queue() {
		return new Queue(ARQUIVO_PROCESSADO_QUEUE);
	}

	@Bean
	public Binding binding() {
		return BindingBuilder
			.bind(queue())
			.to(directExchange())
			.with(ARQUIVO_ENVIA_DADOS_PARA_BOLETO_ROUTING_KEY);
	}
}
