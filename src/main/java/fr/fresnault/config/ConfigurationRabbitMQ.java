package fr.fresnault.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationRabbitMQ {

	public static final String EXCHANGE_NAME = "properties";
	public static final String QUEUE_NAME = "leboncoin";

	@Bean
	public TopicExchange appExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue appQueue() {
		return new Queue(QUEUE_NAME);
	}

	@Bean
	public Binding declareBinding() {
		return BindingBuilder.bind(appQueue()).to(appExchange()).with("leboncoin");
	}

}