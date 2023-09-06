package com.example.associado.config;

import com.example.common.events.Events;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

//@EnableRabbit
//@Configuration
public class RabbitMQConfig {

//  @Value("${spring.rabbitmq.host}")
//  private String host;
//  @Value("${spring.rabbitmq.username}")
//  private String username;
//  @Value("${spring.rabbitmq.password}")
//  private String password;

  //  @Bean
  public Queue associadoCriadoQueue() {
    return new Queue(Events.Associado.ASSOCIADO_CRIADO_QUEUE_NAME);
  }

  //  @Bean
  public Queue associadoAtualizadoQueue() {
    return new Queue(Events.Associado.ASSOCIADO_ATUALIZADO_QUEUE_NAME);
  }

  //  @Bean
  public Queue associadoExcluirQueue() {
    return new Queue(Events.Associado.ASSOCIADO_EXCLUIR_QUEUE_NAME);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(Events.Associado.ASSOCIADO_PUBSUB_TOPIC);
  }

//  @Bean
//  public ConnectionFactory connectionFactory() {
//    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//    connectionFactory.setHost(host);
//    connectionFactory.setUsername(username);
//    connectionFactory.setPassword(password);
//    return connectionFactory;
//  }
//
//  @Bean
//  public MessageConverter jsonMessageConverter() {
//    return new Jackson2JsonMessageConverter();
//  }
//
//  @Bean
//  public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//    rabbitTemplate.setMessageConverter(jsonMessageConverter());
//    return rabbitTemplate;
//  }

//  @Bean
//  public Queue associadoCriadoQueueEvent() {
//    return new Queue(Events.Associado.CRIADO);
//  }
//
//  @Bean
//  public Queue associadoAtualizadoQueueEvent() {
//    return new Queue(Events.Associado.ATUALIZADO);
//  }
//
//  @Bean
//  public Queue associadoExcluirQueueEvent() {
//    return new Queue(Events.Associado.EXCLUIR);
//  }
//
//  @Bean
//  public Queue associadoExcluidoQueueEvent() {
//    return new Queue(Events.Associado.EXCLUIDO);
//  }

}
